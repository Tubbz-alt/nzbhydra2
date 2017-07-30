package org.nzbhydra.downloader;

import org.nzbhydra.GenericResponse;
import org.nzbhydra.NzbDownloadResult;
import org.nzbhydra.NzbHandler;
import org.nzbhydra.config.DownloaderConfig;
import org.nzbhydra.config.NzbAddingType;
import org.nzbhydra.downloader.exceptions.DownloaderException;
import org.nzbhydra.searching.SearchResultEntity;
import org.nzbhydra.searching.SearchResultItem.DownloadType;
import org.nzbhydra.searching.SearchResultRepository;
import org.nzbhydra.searching.searchrequests.SearchRequest.SearchSource;
import org.nzbhydra.web.UsernameOrIpStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
public abstract class Downloader {

    @Autowired
    private NzbHandler nzbHandler;
    @Autowired
    private SearchResultRepository searchResultRepository;

    protected DownloaderConfig downloaderConfig;

    public void intialize(DownloaderConfig downloaderConfig) {
        this.downloaderConfig = downloaderConfig;
    }

    @Transactional
    public GenericResponse addBySearchResultIds(Set<Long> searchResultIds, String category) {
        NzbAddingType addingType = downloaderConfig.getNzbAddingType();
        int countAddedNzbs = 0;
        try {
            for (Long searchResultId : searchResultIds) {
                if (addingType == NzbAddingType.UPLOAD) {
                    NzbDownloadResult result = nzbHandler.getNzbByGuid(searchResultId, downloaderConfig.getNzbAccessType(), SearchSource.INTERNAL, UsernameOrIpStorage.usernameOrIp.get());
                    addNzb(result.getNzbContent(), result.getTitle(), category);
                } else {
                    SearchResultEntity searchResultEntity = searchResultRepository.getOne(searchResultId);
                    addLink(nzbHandler.getNzbDownloadLink(searchResultId, true, DownloadType.NZB), searchResultEntity.getTitle(), category);
                }

                countAddedNzbs++;
            }

        } catch (DownloaderException e) {
            String message = "Error while adding NZB(s) to downloader: " + e.getMessage();
            if (countAddedNzbs > 0) {
                message += ".\n" + countAddedNzbs + " were added successfully";
            }
            return new GenericResponse(false, message);
        }
        return new GenericResponse(true, null);
    }


    public abstract GenericResponse checkConnection();

    public abstract List<String> getCategories();

    public abstract void addLink(String link, String title, String category) throws DownloaderException;

    public abstract void addNzb(String content, String title, String category) throws DownloaderException;

}
