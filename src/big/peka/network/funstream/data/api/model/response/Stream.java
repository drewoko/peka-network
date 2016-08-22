package big.peka.network.funstream.data.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stream {

    private final User streamer;

    private final String name;

    private final int rating;

    private final String description;

    private final long id;

    private final String image;

    private final String thumbnail; // Относительная ссылка на картинку превью

    private final boolean adult; // Флаг 18+

 //   private final long start_at; // Время начала стрима, 0 по умолчанию

    private final String slug; // Имя стрима в ссылке, /stream/<slug>

    private final boolean hidden;
     // Флаг скрытия стрима в списке


    public Stream(
            @JsonProperty("owner") User streamer,
            @JsonProperty("name") final String name,
            @JsonProperty("rating") final int rating,
            @JsonProperty("description") final String description,
            @JsonProperty("id") final long id,
            @JsonProperty("image") final String image,
            @JsonProperty("thumbnail") final String thumbnail,
            @JsonProperty("adult") final boolean adult,
        //    @JsonProperty("start_at") final long start_at,
            @JsonProperty("slug") final String slug,
            @JsonProperty("hidden") final boolean hidden
    ){
        this.streamer = streamer;
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.id = id;
        this.image = image;
        this.thumbnail = thumbnail;
        this.adult = adult;
      //  this.start_at = start_at;
        this.slug = slug;
        this.hidden = hidden;
    }

    public User getOwner() {
        return streamer;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isAdult() {
        return adult;
    }

   /* public long getStart_at() {
        return start_at;
    }*/

    public String getSlug() {
        return slug;
    }

    public boolean isHidden() {
        return hidden;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        final Stream stream = (Stream) o;

        return !(streamer != null ? !streamer.equals(stream.streamer) : stream.streamer != null);
    }

    @Override
    public int hashCode() {
        // streams are qeual of streamers are equal (stream/streamerId stuff)
        return streamer != null ? streamer.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Stream{" +
                "streamer=" + streamer +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", image='" + image + '\'' +
                '}';
    }
}
