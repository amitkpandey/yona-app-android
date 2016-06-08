package nu.yona.app.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kinnarvasa on 06/06/16.
 */
public class EmbeddedYonaActivity {
    @SerializedName("_embedded")
    @Expose
    private Embedded embedded;
    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("page")
    @Expose
    private Page page;
    @SerializedName("dayActivityList")
    @Expose
    private List<DayActivity> dayActivityList = new ArrayList<>();

    /**
     * Gets embedded.
     *
     * @return The embedded
     */
    public Embedded getEmbedded() {
        return embedded;
    }

    /**
     * Sets embedded.
     *
     * @param embedded The _embedded
     */
    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    /**
     * Gets links.
     *
     * @return The links
     */
    public Links getLinks() {
        return links;
    }

    /**
     * Sets links.
     *
     * @param links The _links
     */
    public void setLinks(Links links) {
        this.links = links;
    }

    /**
     * Gets page.
     *
     * @return The page
     */
    public Page getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param page The page
     */
    public void setPage(Page page) {
        this.page = page;
    }

    public List<DayActivity> getDayActivityList() {
        return this.dayActivityList;
    }

    public void setDayActivityList(List<DayActivity> dayActivityList) {
        this.dayActivityList = dayActivityList;
    }

}
