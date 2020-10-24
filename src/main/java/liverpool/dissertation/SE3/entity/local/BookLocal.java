package liverpool.dissertation.SE3.entity.local;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class BookLocal {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(length=300)
	private String titleStem;
	
	private long databaseId;
	
	private String encryptionIV;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getTitleStem() {
		return titleStem;
	}
	public void setTitleStem(String titleStem) {
		this.titleStem = titleStem;
	}
	
	public long getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(long databaseId) {
		this.databaseId = databaseId;
	}

	public String getEncryptionIV() {
		return encryptionIV;
	}
	public void setEncryptionIV(String encryptionIV) {
		this.encryptionIV = encryptionIV;
	}
	
	@Override
	public String toString() {
		return this.titleStem;
	}
}
