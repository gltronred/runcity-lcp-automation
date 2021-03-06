package org.runcity.mvc.web.tabledata;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ConsumerRole;
import org.runcity.db.entity.enumeration.SecureUserRole;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.ChangePasswordByIdForm;
import org.runcity.mvc.web.formdata.ConsumerCreateForm;
import org.runcity.mvc.web.formdata.ConsumerEditForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormListboxActiveColumn;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class ConsumerTable extends AbstractTable {
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView(Views.Public.class)
		private Long id;

		@JsonView(Views.Public.class)
		private String username;

		@JsonView(Views.Public.class)
		private String credentials;

		@JsonView(Views.Public.class)
		private String email;

		@JsonView(Views.Public.class)
		private String active;

		@JsonView(Views.Public.class)
		private String roles;

		public TableRow(Consumer c) {
			this.id = c.getId();
			this.username = StringUtils.xss(c.getUsername());
			this.credentials = StringUtils.xss(c.getCredentials());
			this.email = StringUtils.xss(c.getEmail());
			this.active = new FormListboxActiveColumn().getOptionDisplay(c.isActive().toString(), messageSource, locale);
			
			List<String> rolesTmp = new LinkedList<String>();
			for (ConsumerRole r : c.getRoles()) {
				rolesTmp.add(SecureUserRole.getDisplayName(r.getRole(), messageSource, locale));
			}
			Collections.sort(rolesTmp);
			this.roles = StringUtils.xss(StringUtils.toString(rolesTmp, messageSource, locale));
		}
	}

	public ConsumerTable(MessageSource messageSource, DynamicLocaleList localeList) {
		super("consumerTable", "user.tableHeader", "user.tableHeader", "/api/v1/consumerTable", messageSource, localeList);
		
		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("username", "user.username"));
		this.columns.add(new ColumnDefinition("credentials", "user.credentials").setSort("asc", 1));
		this.columns.add(new ColumnDefinition("email", "user.email"));
		this.columns.add(new ColumnDefinition("active", "user.active"));
		this.columns.add(new ColumnDefinition("roles", "user.roles"));
		
		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "createform:consumerCreateForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:consumerEditForm:id", "selectedSingle"));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/consumerDelete/:id", "selected"));
		this.buttons.add(new ButtonDefinition("changePassword.header", null, "btn", "form:changePasswordByIdForm:id", "selected"));

		this.relatedForms.add(new ConsumerCreateForm(localeList));
		this.relatedForms.add(new ConsumerEditForm(localeList));
		this.relatedForms.add(new ChangePasswordByIdForm());

		this.expandFrame = "/secure/iframe/consumer/{0}:id";
	}

	public void fetchAll(ConsumerService service) {
		List<Consumer> consumers = service.selectAll(Consumer.SelectMode.WITH_ROLES);
		for (Consumer c : consumers) {
			data.add(new TableRow(c));
		}
	}

	public List<TableRow> getData() {
		return data;
	}
	
	@Override
	public ConsumerTable validate() {
		return this;
	}
}
