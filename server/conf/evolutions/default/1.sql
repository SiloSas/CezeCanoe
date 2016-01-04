# --- !Ups
CREATE TABLE rooms (
  id                        VARCHAR PRIMARY KEY,
  name                      VARCHAR,
  presentation              VARCHAR,
  header                    VARCHAR,
  images                    VARCHAR,
  isAnApartment             BOOLEAN,
  price                     VARCHAR
);

INSERT INTO rooms(id, name, presentation, header, images, isAnApartment, price) VALUES
  ('a4aea509-1002-47d0-b55c-593c91ce32ae', 'dsdlksd', 'kdskdjsk', 'kdskdjsk', 'assets/images/desToits1.jpg', false, '5€');


CREATE TABLE descentes (
  id                        VARCHAR PRIMARY KEY,
  name                      VARCHAR NOT NULL,
  presentation              VARCHAR NOT NULL,
  tour                      VARCHAR NOT NULL,
  images                    VARCHAR NOT NULL,
  distance                  VARCHAR NOT NULL,
  time                      VARCHAR NOT NULL
);

INSERT INTO descentes(id, name, presentation, tour, images, distance, time) VALUES
  ('a4aea509-1002-47d0-b55c-593c91cb32ae',
  '[{"lang": "Fr", "presentation": "Les Rochers de St Gély"}, {"lang": "En", "presentation": "Les Rochers de St Gély en"}]',
  '[{"lang": "Fr", "presentation": "<p>Sur ce parcours de <b>7 km</b> (2h non-stop) vous pouvez garder le canoë 3 ou 4 heures pour pique-niquer et vous baigner. Riche en faune et en flore, la rivière vous offrira un agréable moment de détente. Rendez-vous à notre base de Goudargues au centre du village,<br/>et <b>partez immédiatement de 9h à 16h !</b></p>"}, {"lang": "En", "presentation": "<p>Discover the valley of the Ceze river between Goudargues and Cazernau all along this 7 km trip, with a duration of about 2 hours if you choose to do it without any stops. You just have to show up in our base of Goudargues in the village center, then you can go immediately from 9am to 4pm !"}]',
  '[{"lang": "Fr", "presentation": "...de Goudargues à Cazernau"}, {"lang": "En", "presentation": "...from Goudargues to Cazernau"}]',
  'assets/images/img1.jpg, assets/images/img2.jpg',
  '[{"lang": "Fr", "presentation": "7 km"}, {"lang": "En", "presentation": "7 km"}]',
  '[{"lang": "Fr", "presentation": "1/2 journée"}, {"lang": "En", "presentation": "1/2 day"}]');


CREATE TABLE prices (
  id                      VARCHAR,
  name                    VARCHAR,
  price                   NUMERIC,
  isBookable              BOOLEAN,
  medias                  VARCHAR,
  isSupplement            BOOLEAN
);

INSERT INTO prices(id, name, price, isBookable, medias, isSupplement) VALUES
  ('1', '[{"lang": "Fr", "presentation": "adulte en canoe"}, {"lang": "En", "presentation": "adult with canoe"}]', 9.0, true, '', false);


INSERT INTO prices(id, name, price, isBookable, medias, isSupplement) VALUES
  ('2', '[{"lang": "Fr", "presentation": "enfant en canoe"}, {"lang": "En", "presentation": "child with canoe"}]', 6.0, true, '', false);


INSERT INTO prices(id, name, price, isBookable, medias, isSupplement) VALUES
  ('3', '[{"lang": "Fr", "presentation": "adult gichet"}, {"lang": "En", "presentation": "adult"}]', 9.8, false, '', false);

INSERT INTO prices(id, name, price, isBookable, medias, isSupplement) VALUES
  ('4', '[{"lang": "Fr", "presentation": "adult gichet"}, {"lang": "En", "presentation": "adult"}]', 9.8, true, '', true);

CREATE TABLE descentePriceRelations (
  descenteId              VARCHAR,
  priceId                 VARCHAR
);

INSERT INTO descentePriceRelations(descenteId, priceId) VALUES
  ('a4aea509-1002-47d0-b55c-593c91cb32ae', '1');

INSERT INTO descentePriceRelations(descenteId, priceId) VALUES
  ('a4aea509-1002-47d0-b55c-593c91cb32ae', '2');

INSERT INTO descentePriceRelations(descenteId, priceId) VALUES
  ('a4aea509-1002-47d0-b55c-593c91cb32ae', '3');

# --- !Downs
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS descentes;
DROP TABLE IF EXISTS descentePriceRelations;
DROP TABLE IF EXISTS prices;