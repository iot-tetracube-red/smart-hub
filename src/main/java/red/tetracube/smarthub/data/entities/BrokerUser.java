package red.tetracube.smarthub.data.entities;

public class BrokerUser {

    private Long id;
    private boolean isSuperUser;
    private String clientId;
    private String username;
    private String password;
    private String salt;

    public BrokerUser(Long id,
                      boolean isSuperUser,
                      String clientId,
                      String username,
                      String password) {
        this.id = id;
        this.isSuperUser = isSuperUser;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.salt = null;
    }

    public Long getId() {
        return id;
    }

    public boolean isSuperUser() {
        return isSuperUser;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }
}
