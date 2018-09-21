package reports.domain;

public class UserTokenState {
    private String access_token;
    private String user;
    private Integer roles;
    private Long expires_in;

    public UserTokenState() {
        this.access_token = null;
        this.expires_in = null;
    }

    public UserTokenState(String access_token, String user, Integer roles, long expires_in) {
        this.access_token = access_token;
        this.user = user;
        this.roles = roles;
        this.expires_in = expires_in;
    }

    public String getUser() {return user;}

    public void setUser(String user) {this.user = user;}

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public Integer getRoles() {return roles;}

    public void setRoles(Integer roles) {this.roles = roles;}
}