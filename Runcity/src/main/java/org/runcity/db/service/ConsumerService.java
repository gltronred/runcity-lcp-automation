package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.Token;
import org.runcity.exception.DBException;
import org.runcity.exception.EMailException;
import org.runcity.util.CommonProperties;
import org.springframework.context.MessageSource;

public interface ConsumerService {
	public List<Consumer> selectAll(Consumer.SelectMode selectMode);

	public Consumer selectByUsername(String username, Consumer.SelectMode selectMode);

	public Consumer selectByEmail(String email, Consumer.SelectMode selectMode);
	
	public Consumer selectById(Long id, Consumer.SelectMode selectMode);
	
	public Consumer add(Consumer c) throws DBException;
	
	public Consumer update(Consumer c) throws DBException;
		
	public void delete(List<Long> id);
	
	public boolean validatePassword(Consumer c, String password);
	
	public Consumer register(String username, String password, String credentials, String email, String locale) throws DBException;
	
	public Consumer getCurrent();
	
	public Consumer updateCurrentData(String username, String credentials, String email, String locale);

	public Consumer updateCurrentPassword(String newPassword) throws DBException;
	
	public List<Consumer> updatePassword(List<Long> id, String newPassword) throws DBException;

	public void recoverPassword(Consumer c, CommonProperties commonProperties, MessageSource messageSource) throws DBException, EMailException;
	
	public Token getPasswordResetToken(String token, String check);
	
	public void invalidateRecoveryTokens(Consumer c) throws DBException;
	
	public Consumer resetPasswordByToken(Token token, String password) throws DBException;
}
