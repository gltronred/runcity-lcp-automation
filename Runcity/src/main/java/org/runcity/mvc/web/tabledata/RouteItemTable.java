package org.runcity.mvc.web.tabledata;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.RouteItemCreateEditForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class RouteItemTable extends AbstractTable {
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();
	
	protected class TableRow {
		@JsonView(Views.Public.class)
		private Long id;
		
		@JsonView(Views.Public.class)
		private Integer sort;
		
		@JsonView(Views.Public.class)
		private Integer legNum;

		@JsonView(Views.Public.class)
		private String type;
		
		@JsonView(Views.Public.class)
		private String idt;
		
		@JsonView(Views.Public.class)
		private String name;
		
		@JsonView(Views.Public.class)
		private String address;
		
		public TableRow(RouteItem ri, MessageSource messageSource, Locale l) {
			this.id = ri.getId();
			this.legNum = ri.getLegNumber();
			if (ri.getControlPoint() != null) {
				switch (ri.getControlPoint().getType()) {
				case START:
					this.sort = 0;
					this.legNum = null;
					break;
				case BONUS:
					this.sort = ri.getLegNumber() == null ? 0 : ri.getLegNumber() * 10;
					break;
				case REGULAR:
					this.sort = ri.getLegNumber() == null ? 0 : ri.getLegNumber() * 10 + 1;
					break;
				case STAGE_END:
					this.sort = ri.getLegNumber() == null ? 0 : ri.getLegNumber() * 10 + 9;
					break;
				case FINISH:
					this.sort = 9999;
					this.legNum = null;
					break;
				}
			}
			this.idt = StringUtils.xss(ri.getControlPoint().getIdt());
			this.type = StringUtils.xss(ri.getControlPoint().getType().getDisplayName(messageSource, l));
			this.name = StringUtils.xss(ri.getControlPoint().getName());
			this.address = StringUtils.xss(ri.getControlPoint().getLocalizedAddress(l.toString()));
		}

		public Long getId() {
			return id;
		}

		public Integer getSort() {
			return sort;
		}

		public Integer getLegNum() {
			return legNum;
		}

		public String getIdt() {
			return idt;
		}

		public String getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public String getAddress() {
			return address;
		}
	}
	
	public RouteItemTable(MessageSource messageSource, DynamicLocaleList localeList, Route route) {
		super("routeItemTable", "routeItem.tableHeader", "routeItem.tableHeader", "/api/v1/routeItemTable?routeId=" + route.getId(), messageSource, localeList);

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("sort", null).setHidden(true).setSort("asc", 0));
		this.columns.add(new ColumnDefinition("legNum", "routeItem.leg"));
		this.columns.add(new ColumnDefinition("idt", "controlPoint.idt").setSort("asc", 1));
		this.columns.add(new ColumnDefinition("type", "controlPoint.type"));
		this.columns.add(new ColumnDefinition("name", "controlPoint.name"));
		this.columns.add(new ColumnDefinition("address", "controlPoint.address"));
		
		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "createform:routeItemCreateEditForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:routeItemCreateEditForm:id", "selectedSingle"));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/routeItemDelete/:id", "selected")); 
		
		RouteItemCreateEditForm form = new RouteItemCreateEditForm(localeList);
		form.setRouteId(route.getId());
		this.relatedForms.add(form);
	}
	
	public void fill(Route gc) {
		for (RouteItem ri : gc.getRouteItems()) {
			data.add(new TableRow(ri, messageSource, locale));
		}
	}
	
	public List<TableRow> getData() {
		return data;
	}
}