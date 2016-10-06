package sk.tsystems.gamestudio.entity;
import javax.persistence.*;

@Entity
@Table(name="JPA_USER")
public class UserEntity {
	@Id
	@Column(name = "USRID")
	@GeneratedValue
	private int id;
	
	@Column(name = "USRNAME")	
	private String name;
	
	@Column(name = "PASSW")	
	private String password;
	
	@Column(name = "MAIL")
	private String mail;
	
	public UserEntity() // constructor for JPA
	{
		this(0, "", "");
	}

	public UserEntity(int id, String name, String mail) {
		super();
		this.id = id;
		this.name = name;
		this.mail = mail;
	}
	
	public UserEntity(int id, String name) {
		this(id, name, "");
	}
	

	public int getID() {
		return id;
	}
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}


	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", name=" + name + "]";
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
