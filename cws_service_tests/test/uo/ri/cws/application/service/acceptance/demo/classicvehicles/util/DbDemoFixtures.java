package uo.ri.cws.application.service.acceptance.demo.classicvehicles.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.builders.TVehiclesRecordBuilder;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVehicleTypesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVehiclesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.util.random.Random;

public class DbDemoFixtures {

	public static TWorkOrdersRecord aWorkOrderWithClassicVehicleAndOneFinishedWorkorder() {
		TClientsRecord client = DbFixtures.aClient();
		TVehicleTypesRecord vehicleType = DbFixtures.aVehicleType();
		TVehiclesRecord vehicle = aVehicleClassicForClientAndType(client.id, vehicleType.id);
		TWorkOrdersRecord wo = DbFixtures.aWorkOrderApprovedForVehicle(vehicle.id);

		wo.total_amount = BigDecimal.valueOf( Random.inRange(100.0, 1500.0) ).setScale(2, RoundingMode.HALF_EVEN);
		Db.update(wo);

		return wo;
	}

	private static TVehiclesRecord aVehicleClassicForClientAndType(String cId, String vtId) {
		TVehiclesRecord vehicle = new TVehiclesRecordBuilder()
				.forClientId(cId)
				.forVehicleTypeId(vtId)
				.withRegistrationDate( Date.valueOf("1965-05-15") )
				.build();
		Db.insert(vehicle);
		return vehicle;
	}

}
