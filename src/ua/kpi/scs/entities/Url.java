package ua.kpi.scs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
		@NamedQuery(name = Url.GET_NOT_PARSED_URL, query = "SELECT p FROM Url p WHERE p.parsed = false"),
		@NamedQuery(name = Url.GET_URL_BY_ADDRESS, query = "SELECT p FROM Url p WHERE p.url=:url") })
@Table(name = "urls")
public class Url implements Serializable {

	public static final String GET_NOT_PARSED_URL = "notParced";
	public static final String GET_URL_BY_ADDRESS = "urlByAddress";
	public static final int MAX_URL_LENGTH = 255;

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "CONTENT")
	private String content;

	@Column(name = "PARENT_URL_ID")
	private int parentUrlId;

	@Column(name = "URL", length = MAX_URL_LENGTH, unique = true)
	private String url;

	@Column(name = "PARSED")
	private boolean parsed;

	@Basic(optional = false)
	@Column(name = "LAST_UPDATE", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;

	public Url() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getParentUrlId() {
		return this.parentUrlId;
	}

	public void setParentUrlId(int parentUrlId) {
		this.parentUrlId = parentUrlId;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isParsed() {
		return parsed;
	}

	public void setParsed(boolean parsed) {
		this.parsed = parsed;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

}