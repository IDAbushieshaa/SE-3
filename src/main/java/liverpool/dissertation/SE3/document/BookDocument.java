package liverpool.dissertation.SE3.document;

import javax.persistence.Id;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "BOOKS-SE-2")
public class BookDocument {
	
	@Id
	@Indexed(name="id", type="string")
	private String solrId;
	
	
	@Indexed(name="TITLE", type="string")
	private String title;
	
	
	@Field(value="DB_ID")
	private String databaseId;
	
	@Field(value="ENCRYPTION_KEY")
	private String encryptionKey;
	
	@Field(value="ENCRYPTION_IV")
	private String encryptionSalt;


	public String getSolrId() {
		return solrId;
	}
	public void setSolrId(String solrId) {
		this.solrId = solrId;
	}


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	public String getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}
	
	
	public String getEncryptionKey() {
		return encryptionKey;
	}
	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	
	public String getEncryptionSalt() {
		return encryptionSalt;
	}
	public void setEncryptionSalt(String encryptionSalt) {
		this.encryptionSalt = encryptionSalt;
	}
	
	@Override
	public String toString() {
		return "Database ID = " + databaseId + " & solrId = " + solrId;
	}
}
