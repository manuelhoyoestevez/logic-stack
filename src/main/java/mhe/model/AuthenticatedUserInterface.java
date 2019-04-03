package mhe.model;

import java.util.Collection;

public interface AuthenticatedUserInterface {
	public String getId();
	public String getEmail();
	public Boolean hasRole(String role);
	public Boolean hasRoles(Collection<String> roles);
	public Boolean hasPermission(Integer permissionId);
	public Collection<String> getRoles();
	public Collection<Integer> getPermissions();
	public String getJWT();
}
