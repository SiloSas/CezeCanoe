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

-- INSERT INTO rooms(id, name, presentation, header, images, isAnApartment, price) VALUES
--   ('a4aea509-1002-47d0-b55c-593c91ce32ae', 'dsdlksd', 'kdskdjsk', 'kdskdjsk', 'assets/images/desToits1.jpg', false, '5€');


CREATE TABLE descentes (
  id                        VARCHAR PRIMARY KEY,
  name                      VARCHAR NOT NULL,
  presentation              VARCHAR NOT NULL,
  tour                      VARCHAR NOT NULL,
  images                    VARCHAR NOT NULL,
  distance                  VARCHAR NOT NULL,
  prices                    VARCHAR NOT NULL,
  time                      VARCHAR NOT NULL,
  isVisible                 BOOLEAN,
  groupReduction            NUMERIC
);

-- INSERT INTO descentes(id, name, presentation, tour, images, distance, prices, time, isVisible, groupReduction) VALUES
--   ('a4aea509-1002-47d0-b55c-593c91cb32ae',
--   '[{"lang": "Fr", "presentation": "Les Rochers de St Gély"}, {"lang": "En", "presentation": "Les Rochers de St Gély en"}]',
--   '[{"lang": "Fr", "presentation": "<p>Sur ce parcours de <b>7 km</b> (2h non-stop) vous pouvez garder le canoë 3 ou 4 heures pour pique-niquer et vous baigner. Riche en faune et en flore, la rivière vous offrira un agréable moment de détente. Rendez-vous à notre base de Goudargues au centre du village,<br/>et <b>partez immédiatement de 9h à 16h !</b></p>"}, {"lang": "En", "presentation": "<p>Discover the valley of the Ceze river between Goudargues and Cazernau all along this 7 km trip, with a duration of about 2 hours if you choose to do it without any stops. You just have to show up in our base of Goudargues in the village center, then you can go immediately from 9am to 4pm !"}]',
--   '[{"lang": "Fr", "presentation": "...de Goudargues à Cazernau"}, {"lang": "En", "presentation": "...from Goudargues to Cazernau"}]',
--   '["assets/images/img1.jpg", "assets/images/img2.jpg"]',
--   '[{"lang": "Fr", "presentation": "7 km"}, {"lang": "En", "presentation": "7 km"}]',
--   '[]',
--   '[{"lang": "Fr", "presentation": "1/2 journée"}, {"lang": "En", "presentation": "1/2 day"}]', TRUE, 10.0);


CREATE TABLE tariffs (
  id                      VARCHAR,
  name                    VARCHAR,
  price                   NUMERIC,
  isBookable              BOOLEAN,
  medias                  VARCHAR,
  isSupplement            BOOLEAN
);

-- INSERT INTO tariffs(id, name, price, isBookable, medias, isSupplement) VALUES
--   ('a4aea509-1002-47d0-b55c-593c91cb32ae', '[{"lang": "Fr", "presentation": "adulte en Kayak"}, {"lang": "En", "presentation": "adult with kayak"}]', 4.0, true,
--    '["assets/images/kayak.jpg", "assets/images/kayak1.jpg"]', true);


CREATE TABLE informations (
  id                      VARCHAR,
  information             VARCHAR
);

-- INSERT INTO informations(id, information) VALUES
--   ('a4aea509-1002-47d0-b55c-593c91cb32ae',
--   '[{"lang": "Fr", "presentation": "Enfant de moins de 30 kg en 3ème place, 6€ sur TOUS nos parcours !<br/> Prix guichet : 7€"}, {"lang": "En", "presentation": "* child under 60 Pounds in a canoe with 2 adults: 6€ for ALL the trips ! </br>7 € on the spot"}]');


CREATE TABLE articles (
  id                      VARCHAR,
  content                 VARCHAR,
  media                   VARCHAR,
  yellowThing              VARCHAR
);

-- INSERT INTO articles(id, content, media, yellowThing) VALUES
--   ('a4aea509-1002-47d0-455c-593c91cb32ae',
--   '[{"lang": "Fr", "presentation": "<h2>La Cèze en canoë ...</h2><p>Découvrez la Cèze et ses gorges, au cours d une balade en canoë ou en kayak.</p><p><b>La Cèze est une rivière paisible et accessible à tous,</b> sur laquelle vous pourrez naviguer en famille"},{"lang": "En", "presentation": "adult with kayak"}]',
--   'https://www.youtube.com/embed/7foc3g23ROA',
--   '[{"lang": "Fr", "presentation": ""},{"lang": "En", "presentation": ""}]');

CREATE TABLE homeImages (
  images                      VARCHAR
);

-- INSERT INTO homeImages(images) VALUES('["assets/images/img1.jpg", "assets/images/img2.jpg"]');

CREATE TABLE users (
  id                        SERIAL PRIMARY KEY,
  login                     VARCHAR(50),
  password                  VARCHAR(100)
);
-- INSERT INTO users(login, password) VALUES('admin', '$2a$07$8SJ.wfjn2IaidQVHfcmrHuWzrknBqJE8f.8BO7fu.W.d5u0W5r3t.');

CREATE TABLE booking (
id VARCHAR ,
descentId VARCHAR,
clientForm VARCHAR,
details VARCHAR,
isGroup BOOLEAN
);

CREATE TABLE services (
id VARCHAR ,
content VARCHAR,
images VARCHAR
);

CREATE TABLE occasions (
id VARCHAR ,
content VARCHAR,
images VARCHAR
);

CREATE TABLE groups (
id VARCHAR ,
content VARCHAR,
images VARCHAR
);

CREATE TABLE partners (
id VARCHAR ,
content VARCHAR,
media VARCHAR,
link VARCHAR
);

# --- !Downs
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS descentes;
DROP TABLE IF EXISTS informations;
DROP TABLE IF EXISTS tariffs;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS homeImages;
DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS occasions;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS partners;