CREATE TABLE dealers (
                         id UUID PRIMARY KEY,
                         corporate_name VARCHAR(255) NOT NULL,
                         cnpj VARCHAR(20) NOT NULL UNIQUE,
                         zip_code VARCHAR(9) NOT NULL,
                         address VARCHAR(255) NOT NULL
);

CREATE TABLE vehicles (
                          id UUID PRIMARY KEY,
                          brand VARCHAR(100) NOT NULL,
                          model VARCHAR(100) NOT NULL,
                          color VARCHAR(100) NOT NULL,
                          year INTEGER,
                          price NUMERIC(12, 2),
                          dealer_id UUID NOT NULL,

                          CONSTRAINT fk_vehicle_dealer
                              FOREIGN KEY (dealer_id)
                                  REFERENCES dealers(id)
);

CREATE TABLE vehicle_fuel_types (
                                    vehicle_id UUID NOT NULL,
                                    fuel_type VARCHAR(30) NOT NULL,

                                    CONSTRAINT fk_vehicle_fuel_type_vehicle
                                        FOREIGN KEY (vehicle_id)
                                            REFERENCES vehicles(id)
                                            ON DELETE CASCADE,

                                    CONSTRAINT pk_vehicle_fuel_types
                                        PRIMARY KEY (vehicle_id, fuel_type)
);