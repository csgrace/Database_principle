package io.pubmed.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The article_id information class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Author implements Serializable{
    private String fore_name;
    private String last_name;
    private String initials;
    private String collective_name;

    // 假设作者的 ID 是根据其他字段计算或返回的
    public int getAuthorId() {
        String foreNameValue = (fore_name != null) ? fore_name : "";
        String lastNameValue = (last_name != null) ? last_name : "";
        String initialsValue = (initials != null) ? initials : "";
        return (foreNameValue + lastNameValue + initialsValue).hashCode();
    }

    public String getFore_name() {
        return fore_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getInitials() {
        return initials;
    }
}

