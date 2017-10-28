package org.runcity.mvc.web.util;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;

public class FormPasswordColumn extends FormStringColumn {
	private FormStringColumn passwordConfirmation;
	
	private static final String PWD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
	private Pattern pattern = Pattern.compile(PWD_PATTERN);

	public FormPasswordColumn(Long id, ColumnDefinition definition, String formName, boolean required) {
		super(id, definition, formName, required, null, null);
	}

	public FormPasswordColumn(Long id, ColumnDefinition definition, String formName, boolean required, String value) {
		super(id, definition, formName, required, null, null, value);
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);
		
		if (!pattern.matcher(value).matches()) {
			errors.rejectValue(getName(), "js.passwordStrength");
		}
	}
	
	public void setPasswordConfirmation(FormPasswordConfirmationColumn passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	@Override
	public String getJsChecks() {
		return super.getJsChecks() + "pwd;";
	}
	
	@Override
	public String getOnChange() {
		return "checkPwdInput('" + getHtmlId() + "', '" + passwordConfirmation.getHtmlId() + "', translations)";
	}
}
