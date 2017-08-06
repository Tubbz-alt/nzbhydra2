package org.nzbhydra.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@SuppressWarnings("unchecked")
@Data
public class SearchingConfig extends ValidatingConfig {

    private boolean alwaysShowDuplicates;
    @JsonFormat(shape = Shape.STRING)
    private SearchSourceRestriction applyRestrictions = SearchSourceRestriction.BOTH;
    //categorySettings ;
    private float duplicateAgeThreshold = 2.0F;
    private float duplicateSizeThresholdInPercent = 1.0F;
    private boolean enableCategorySizes = true;
    private List<String> forbiddenGroups = new ArrayList<>();
    private List<String> forbiddenPosters = new ArrayList<>();
    private String forbiddenRegex;
    private List<String> forbiddenWords = new ArrayList<>();
    private SearchSourceRestriction generateQueries = SearchSourceRestriction.NONE;
    @JsonFormat(shape = Shape.STRING)
    private SearchSourceRestriction idFallbackToQueryGeneration = SearchSourceRestriction.NONE;
    private boolean ignorePassworded = false;
    private boolean ignoreTemporarilyDisabled = false;
    private int keepSearchResultsForDays = 14;
    private Integer maxAge;
    @JsonFormat(shape = Shape.STRING)
    private NzbAccessType nzbAccessType = NzbAccessType.REDIRECT;
    private List<String> removeTrailing = new ArrayList<>();
    private String requiredRegex;
    private List<String> requiredWords = new ArrayList<>();
    private Integer timeout = 30;
    private String userAgent = "NZBHydra2";
    private boolean useOriginalCategories = false;
    private boolean wrapApiErrors = false;

    public SearchingConfig() {
        //removeTrailing = new ArrayList<>(Arrays.asList(".mp4", ".mkv", ".subs", ".REPOST", "repost", "~DG~", ".DG", "-DG", "-1", ".1", "(1)", "ReUp", "ReUp2", "-RP", "-AsRequested", "-Obfuscated", "-Scrambled", "-Chamele0n", "-BUYMORE", "-[TRP]", "-DG", ".par2", ".part01", "part01.rar", ".part02.rar", ".jpg", "[rartv]", "[rarbg]", "[eztv]", "English", "Korean", "Spanish", "French", "German", "Italian", "Danish", "Dutch", "Japanese", "Cantonese", "Mandarin", "Russian", "Polish", "Vietnamese", "Swedish", "Norwegian", "Finnish", "Turkish", "Portuguese", "Flemish", "Greek", "Hungarian"));
    }

    public Optional<Integer> getMaxAge() {
        return Optional.ofNullable(maxAge);
    }

    @Override
    public ConfigValidationResult validateConfig(BaseConfig oldConfig) {
        List<String> errors = new ArrayList<>();
        checkRegex(errors, requiredRegex, "The required regex in \"Searching\" is invalid");
        checkRegex(errors, forbiddenRegex, "The forbidden in \"Searching\" is invalid");
        return new ConfigValidationResult(errors.isEmpty(), false, errors, Collections.emptyList());
    }
};
