package com.dubaidrums.jems.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubaidrums.jems.domain.JemsRole;
import com.dubaidrums.jems.domain.JemsUser;
import com.dubaidrums.jems.repository.JemsUserRepository;
import com.dubaidrums.jems.service.JemsUserService;

@Service
@Transactional
public class JemsUserServiceImpl implements JemsUserService {

	Logger log = LogManager.getLogger(JemsUserServiceImpl.class);

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		JemsUser ju = jemsUserRepository.findByUserName(username);
		log.info("method: loadUserByUsername, username: " + username
				+ " userFound: " + (ju != null));
		if (ju != null) {
			if (ju.getRoles().size() == 0)
				throw new UsernameNotFoundException("User has no roles");

			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (JemsRole role : ju.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
			}

			User u = new User(username, ju.getPassword(), ju.getEnabled(),
					true, true, true, authorities);
			return u;
		} else {
			throw new UsernameNotFoundException("User not found");
		}
	}

	public String encrypt(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(text.getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			log.error("method: encrypt, msg: Cannot encrypt text.", e);
			return "";
		}
	}

	@Autowired
	JemsUserRepository jemsUserRepository;

	public long countAllJemsUsers() {
		return jemsUserRepository.count();
	}

	public void deleteJemsUser(JemsUser jemsUser) {
		jemsUserRepository.delete(jemsUser);
	}

	public JemsUser findJemsUser(Long id) {
		return jemsUserRepository.findOne(id);
	}

	public List<JemsUser> findAllJemsUsers() {
		return jemsUserRepository.findAll();
	}

	public List<JemsUser> findJemsUserEntries(int firstResult, int maxResults) {
		return jemsUserRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	public void saveJemsUser(JemsUser jemsUser) {
		jemsUserRepository.save(jemsUser);
	}

	public JemsUser updateJemsUser(JemsUser jemsUser) {
		return jemsUserRepository.save(jemsUser);
	}

	public List<JemsUser> findEnabledJemsUsers() {
		return jemsUserRepository.findByEnabled(true);
	}

	@Override
	public JemsUser findJemsUserByUserName(String userName) {
		return jemsUserRepository.findByUserName(userName);
	}

	@Override
	public JemsUser findJemsUserByUserNameIgnoreCase(String userName) {
		return jemsUserRepository.findByUserNameIgnoreCase(userName);
	}
}
