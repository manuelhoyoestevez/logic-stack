package lotto.model;

import java.util.Collection;

public class AuthenticatedUser implements AuthenticatedUserInterface {
	private String jwt;
	private String id;
	private String email;
	private Collection<String> roles;
	private Collection<Integer> permissions;

	public AuthenticatedUser(String id, String email, Collection<String> roles, Collection<Integer> permissions, String jwt) {
		super();
		this
		.setId(id)
		.setEmail(email)
		.setRoles(roles)
		.setPermissions(permissions)
		.setJWT(jwt);
	}
	
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getEmail() {
		return this.email;
	}
	
	@Override
	public Boolean hasRole(String role) {
		return this.getRoles().contains(role);
	}
	
	@Override
	public Boolean hasPermission(Integer permissionId) {
		return this.permissions.contains(permissionId);
	}

	@Override
	public Boolean hasRoles(Collection<String> requiredRoles) {
		for(String role : requiredRoles) {
			if(!this.hasRole(role)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public Collection<String> getRoles() {
		return this.roles;
	}
	
	@Override
	public Collection<Integer> getPermissions() {
		return this.permissions;
	}
	
	@Override
	public String getJWT() {
		return this.jwt;
	}

	protected AuthenticatedUser setId(String id) {
		this.id = id;
		return this;
	}

	protected AuthenticatedUser setEmail(String email) {
		this.email = email;
		return this;
	}

	protected AuthenticatedUser setRoles(Collection<String> roles) {
		this.roles = roles;
		return this;
	}
	
	protected AuthenticatedUser setPermissions(Collection<Integer> permissions) {
		this.permissions = permissions;
		return this;
	}
	
	protected AuthenticatedUser setJWT(String jwt) {
		this.jwt = jwt;
		return this;
	}

	@Override
	public String toString() {
		String ret = "{ ID: " + this.getId() + ", ";
		ret += "Email: " + this.getEmail() + ", ";
		ret += "Roles: " + this.getRoles() + ", ";
		ret += "Permissions: " + this.getPermissions() + " }";
		return ret;
	}
}
