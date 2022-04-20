package mon7.project.bookstore.provider.model.view;

import mon7.project.bookstore.provider.model.Provider;

public class ProviderPreview {
    private Long id;
    private String displayName;
    private String address;

    public ProviderPreview() {
    }

    public ProviderPreview(Long id, String displayName, String address) {
        this.id = id;
        this.displayName = displayName;
        this.address = address;
    }

    public ProviderPreview(Provider provider){
        this.id = provider.getId();
        this.displayName = provider.getDisplayName();
        this.address = provider.getAddress();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
