package liverpool.dissertation.SE3.response;

public class AddBooksResponse {
	
	private boolean success;
	private String status;
	
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
