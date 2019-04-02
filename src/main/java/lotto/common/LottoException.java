package lotto.common;

public class LottoException extends Exception {
	private static final long serialVersionUID = -2280577056924430223L;
	private Integer status = null;;
	private String code = null;
	private String body = null;
	
	public LottoException(Integer status, String code, String message, String body) {
		super(message);
		this.code = code;
		this.body = body;
		this.status = status;
	}
	
	public LottoException(Integer status, String code, String message) {
		this(status, code, message, null);
	}
	
	public String getCode() {
		return this.code;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	
	public String getBody() {
		return this.body;
	}
}
