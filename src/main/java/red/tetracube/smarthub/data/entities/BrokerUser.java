package red.tetracube.smarthub.data.entities;

public class BrokerUser {

    private Long id;
    private boolean isSuperUser;
    private String clientId;
    private String username;
    private String password;
    private String salt;

    public static BrokerUser getNewUser(String clientId,
                      String username,
                      String password) {
        var brokerUser = new BrokerUser();
        brokerUser.id = 0L;
        brokerUser.isSuperUser = false;
        brokerUser.clientId = clientId;
        brokerUser.username = username;
        brokerUser.password = password;
        brokerUser.salt = null;
        return brokerUser;
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
