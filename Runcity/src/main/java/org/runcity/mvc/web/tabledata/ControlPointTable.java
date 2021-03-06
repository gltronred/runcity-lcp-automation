package org.runcity.mvc.web.tabledata;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Translation;
import org.runcity.db.entity.enumeration.ControlPointType;
import org.runcity.db.service.ControlPointService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.ControlPointCreateEditByGameForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class ControlPointTable extends AbstractTable {
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView(Views.Public.class)
		private Long id;

		@JsonView(Views.Public.class)
		private Long gameId;

		@JsonView(Views.Public.class)
		private String mainIdt;

		@JsonView(Views.Public.class)
		private String idt;

		@JsonView(Views.Public.class)
		private String type;

		@JsonView(Views.Public.class)
		private String name;

		@JsonView(Views.Public.class)
		private Map<String, String> address;

		@JsonView(Views.Public.class)
		private Long image;

		@JsonView(Views.Public.class)
		private String description;

		public TableRow(ControlPoint c) {
			this.id = c.getId();
			this.gameId = c.getGame().getId();
			this.mainIdt = c.getParent() == null ? StringUtils.xss(c.getIdt())
					: StringUtils.xss(c.getParent().getIdt());
			this.idt = StringUtils.xss(c.getIdt());
			this.type = StringUtils.xss(ControlPointType.getDisplayName(c.getType(), messageSource, locale));
			this.name = StringUtils.xss(c.getName());

			this.address = localeList.prepareMap();
			for (Translation t : c.getAddresses()) {
				if (localeList.contains(t.getLocale())) {
					this.address.put(t.getLocale(), StringUtils.xss(t.getContent()));
				}
			}

			this.image = c.getImage();
			this.description = StringUtils.xss(c.getDescription());
		}
	}

	public ControlPointTable(MessageSource messageSource, DynamicLocaleList localeList, Game g) {
		super("controlPointTable", "controlPoint.tableHeader", "controlPoint.simpleTableHeader",
				"/api/v1/controlPointsTable?gameId=" + g.getId(), messageSource, localeList, StringUtils.xss(g.getName()));

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("gameId", null).setHidden(true));
		this.columns.add(new ColumnDefinition("mainIdt", "controlPoint.main").setSort("asc", 1));
		this.columns.add(new ColumnDefinition("idt", "controlPoint.idt").setSort("asc", 2));
		this.columns.add(new ColumnDefinition("type", "controlPoint.type"));
		this.columns.add(new ColumnDefinition("name", "controlPoint.name"));

		addLocalizedColumn(this.extensions, "address", "controlPoint.addressLoc");
		this.extensions.add(new ColumnDefinition("description", "controlPoint.description"));
		this.extensions.add(new ColumnDefinition("image", "controlPoint.image")
				.setImageFormat("/secure/controlPointImage?id={0}:id"));

		this.buttons.add(new ButtonDefinition("actions.create", null, "btn",
				"createform:controlPointCreateEditByGameForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:controlPointCreateEditByGameForm:id",
				"selectedSingle"));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn",
				"ajax:DELETE:/api/v1/controlPointDelete/:id", "selected"));

		ControlPointCreateEditByGameForm createEditForm = new ControlPointCreateEditByGameForm(localeList);
		createEditForm.setGameId(g.getId());
		this.relatedForms.add(createEditForm);

		this.expandFrame = "/secure/iframe/controlPoint/{0}:id";
	}

	public void fetchByGame(ControlPointService service, Game game) {
		List<ControlPoint> controlPoints = service.selectByGame(game, ControlPoint.SelectMode.NONE);
		for (ControlPoint c : controlPoints) {
			data.add(new TableRow(c));
		}
	}

	public List<TableRow> getData() {
		return data;
	}

	@Override
	public ControlPointTable validate() {
		return this;
	}
}
