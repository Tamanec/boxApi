package ru.altarix.marm.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import ru.altarix.marm.utils.TimestampJsonSerializer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Map;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_id_seq")
    @SequenceGenerator(name="document_id_seq", sequenceName = "document_id_seq")
    private Long id;

    private String status;

    private Long uptime;

    @Column(name = "ext_id")
    private String extId;

    @Column(name = "import_date")
    @JsonSerialize(using = TimestampJsonSerializer.class)
    private Timestamp importDate;

    @Column(name = "export_date")
    @JsonSerialize(using = TimestampJsonSerializer.class)
    private Timestamp exportDate;

    @Column(name = "publish_date", updatable = false)
    @JsonSerialize(using = TimestampJsonSerializer.class)
    private Timestamp publishDate;

    private String title;

    private String template;

    @Column(name = "full_path")
    private String fullPath;

    @Column(name = "rule_path")
    private String rullPath;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "name_upper")
    private String nameUpper;

    @Type(type = "jsonb")
    private Map<String, Object> data;

    @PrePersist
    public void onCreate() {
        uptime = System.currentTimeMillis();
        if (publishDate == null) {
            publishDate = new Timestamp(System.currentTimeMillis());
        }
    }

    @PreUpdate
    public void onUpdate() {
        uptime = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public Timestamp getImportDate() {
        return importDate;
    }

    public void setImportDate(Timestamp importDate) {
        this.importDate = importDate;
    }

    public Timestamp getExportDate() {
        return exportDate;
    }

    public void setExportDate(Timestamp exportDate) {
        this.exportDate = exportDate;
    }

    public Timestamp getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getRullPath() {
        return rullPath;
    }

    public void setRullPath(String rullPath) {
        this.rullPath = rullPath;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNameUpper() {
        return nameUpper;
    }

    public void setNameUpper(String nameUpper) {
        this.nameUpper = nameUpper;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
