INSERT INTO `osce`.`administrator` (`email`, `name`, `pre_name`, `version`) VALUES ('foo@bar.com', 'Bar', 'Foo', '0');
INSERT INTO `osce`.`administrator` (`email`, `name`, `pre_name`, `version`) VALUES ('bar@foo.com', 'Foo', 'Bar', '0');
INSERT INTO `osce`.`administrator` (`email`, `name`, `pre_name`, `version`) VALUES ('hansruedi.meier@gmail.com', 'Meier', 'Hansruedi', '0');

INSERT INTO `osce`.`semester` (`semester`, `cal_year`, `version`) VALUES ('0', '2011', '0');

INSERT INTO `osce`.`administrator_semesters` (`administrators`, `semesters`) VALUES ('1', '1');
INSERT INTO `osce`.`administrator_semesters` (`administrators`, `semesters`) VALUES ('2', '1');

INSERT INTO `nationality` (id,nationality,version) VALUES (5,'Schweiz',0);
INSERT INTO `nationality` (id,nationality,version) VALUES (6,'Deutschland',0);
INSERT INTO `nationality` (id,nationality,version) VALUES (7,'Frankreich',0);

INSERT INTO `clinic` (id,city,name,postal_code,street,version) VALUES (3,'Basel','Unispital',4031,'Spitalstr. 21',0);
INSERT INTO `clinic` (id,city,name,postal_code,street,version) VALUES (4,'Basel','FelixPlatter-Spital',4055,'Burgfelderstrasse 101',0);
INSERT INTO `clinic` (id,city,name,postal_code,street,version) VALUES (5,'Liestal','Kantonsspital Liestal',4010,'Rheinstrasse 26',0);
INSERT INTO `clinic` (id,city,name,postal_code,street,version) VALUES (6,'Bruderholz','Bruderholz Spital',4101,'Bruderholz',0);

INSERT INTO `office` (id,email,gender,name,pre_name,telephone,title,version) VALUES (3,'okr@hospital.ch',0,'Aebischer','Hannes','054 852 75 75','Herr',0);
INSERT INTO `office` (id,email,gender,name,pre_name,telephone,title,version) VALUES (4,'oms@krankenhaus.ch',0,'Licht','Rudolf','057 584 52 54','Herr',0);
INSERT INTO `office` (id,email,gender,name,pre_name,telephone,title,version) VALUES (5,'ogk@spital.ch',1,'Heiligenknust','Marianne','078 525 21 58','Frau',0);
INSERT INTO `office` (id,email,gender,name,pre_name,telephone,title,version) VALUES (6,'ocg@samsam.ch',1,'Carolin','Caroline','045 258 52 58','Frau',0);
INSERT INTO `office` (id,email,gender,name,pre_name,telephone,title,version) VALUES (7,'ohb@hospital.ch',1,'Walter','Hans','085 528 54 54','Herr',0);
INSERT INTO `office` (id,email,gender,name,pre_name,telephone,title,version) VALUES (8,'ofg@krankenhaus.ch',0,'Holz','Sebastian','058 545 25 15','Herr',0);

INSERT INTO `doctor` (id,email,gender,name,pre_name,telephone,title,version,clinic,office) VALUES (5,'karl.rath@hospital.ch',0,'Rath','Karl','054 452 54 85','Dr. med.',0,3,3);
INSERT INTO `doctor` (id,email,gender,name,pre_name,telephone,title,version,clinic,office) VALUES (6,'Manuela.Scharrer@krank.ch',1,'Scharrer','Manuela','057 586 25 25','Prof. Dr. med.',0,4,4);
INSERT INTO `doctor` (id,email,gender,name,pre_name,telephone,title,version,clinic,office) VALUES (7,'Gertrude.Kummerlaus@spital.ch',1,'Kummerlaus','Gertrude','075 542 54 25','Dr. med.',0,5,5);
INSERT INTO `doctor` (id,email,gender,name,pre_name,telephone,title,version,clinic,office) VALUES (8,'Cornelius.Grabenschänke@samsam.ch',0,'Grabenschänke','Cornelius','025 525 25 25','Dr. med.',0,6,6);
INSERT INTO `doctor` (id,email,gender,name,pre_name,telephone,title,version,clinic,office) VALUES (9,'Hannes.Bratdurst@hospital.ch',0,'Bratdurst','Hannes','058 856 98 28','Dr. med.',0,3,7);
INSERT INTO `doctor` (id,email,gender,name,pre_name,telephone,title,version,clinic,office) VALUES (10,'Franziska.Grödel@krank.ch',1,'Grödel','Franziska','085 258 25 15','Prof. Dr. med.',0,4,8);

INSERT INTO `profession` (id,profession,version) VALUES (2,'Student/in',0);
INSERT INTO `profession` (id,profession,version) VALUES (3,'Bauarbeiter/in',0);
INSERT INTO `profession` (id,profession,version) VALUES (4,'Sachbearbeiter/in',0);
INSERT INTO `profession` (id,profession,version) VALUES (5,'Lehrer/in',0);
INSERT INTO `profession` (id,profession,version) VALUES (6,'Florist/in',0);
INSERT INTO `profession` (id,profession,version) VALUES (7,'Journalist/in',0);
INSERT INTO `profession` (id,profession,version) VALUES (8,'Rentner/in',0);
INSERT INTO `profession` (id,profession,version) VALUES (9,'Informatiker',0);

INSERT INTO `spoken_language` (id,language_name,version) VALUES (4,'Deutsch',0);
INSERT INTO `spoken_language` (id,language_name,version) VALUES (5,'Französisch',0);
INSERT INTO `spoken_language` (id,language_name,version) VALUES (6,'Italienisch',0);
INSERT INTO `spoken_language` (id,language_name,version) VALUES (7,'Englisch',0);
INSERT INTO `spoken_language` (id,language_name,version) VALUES (8,'Spanisch',0);

INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (2,'2012-02-14 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (3,'2010-07-18 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (4,'2011-05-28 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (5,'2011-01-21 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (6,'2009-09-19 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (7,'2009-11-08 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (8,'2010-05-12 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (9,'2010-07-30 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (10,'2012-05-26 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (11,'2012-03-24 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (12,'2010-12-24 00:00:00',0);
INSERT INTO `anamnesis_form` (id,create_date,version) VALUES (13,'2010-07-18 00:00:00',0);

INSERT INTO `anamnesis_check_title` (id, sort_order, text, version) VALUES (1, 1, 'Konsumverhalten', 0);
INSERT INTO `anamnesis_check_title` (id, sort_order, text, version) VALUES (2, 3, 'Krankengeschichte', 0);
INSERT INTO `anamnesis_check_title` (id, sort_order, text, version) VALUES (3, 2, 'Behandlungsgeschichte', 0);

INSERT INTO `anamnesis_check` (id,sort_order,value,type,user_specified_order,text,version,anamnesis_check_title) VALUES (1,2,'',1,NULL,'Rauchen Sie?',0,1);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,user_specified_order,text,version,anamnesis_check_title) VALUES (2,3,'oft|mittel|selten',2,NULL,'Wie oft rauchen Sie?',0,1);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,user_specified_order,text,version,anamnesis_check_title) VALUES (9,4,'Marlboro|Mary Long|Lucky Strike|Awesomesauce|Winfail',3,NULL,'Welche Zigarettenmarken haben Sie schon geraucht?',0,1);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,user_specified_order,text,version,anamnesis_check_title) VALUES (5,6,'',1,NULL,'Leiden Sie unter Diabetes?',0,2);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,user_specified_order,text,version,anamnesis_check_title) VALUES (6,7,'',1,NULL,'Wurde Ihnen der Blinddarm entfernt?',0,2);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,user_specified_order,text,version,anamnesis_check_title) VALUES (8,8,'Darmende|Kopf|Fuss|Iris',2,NULL,'Woraus wurde Ihnen der Blinddarm entfernt?',0,2);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,user_specified_order,text,version,anamnesis_check_title) VALUES (3,10,'',1,NULL,'Nehmen Sie zurzeit regelmässig Medikamente ein?',0,3);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,user_specified_order,text,version,anamnesis_check_title) VALUES (4,11,'',0,NULL,'Welche Medikamente nehmen Sie ein?',0,3);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,user_specified_order,text,version,anamnesis_check_title) VALUES (7,12,'Prozac|Ritalin|Aspirin|Ethanol',3,NULL,'Nehmen Sie eines der aufgelisteten Medikamete und wenn ja, welche?',0,3);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,text,version,anamnesis_check_title) VALUES (1,1,'',1,'Rauchen Sie?',0,1);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,text,version,anamnesis_check_title) VALUES (2,2,'oft|mittel|selten',2,'Wie oft rauchen Sie?',0,1);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,text,version,anamnesis_check_title) VALUES (9,3,'Marlboro|Mary Long|Lucky Strike|Awesomesauce|Winfail',3,'Welche Zigarettenmarken haben Sie schon geraucht?',0,1);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,text,version,anamnesis_check_title) VALUES (5,1,'',1,'Leiden Sie unter Diabetes?',0,2);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,text,version,anamnesis_check_title) VALUES (6,2,'',1,'Wurde Ihnen der Blinddarm entfernt?',0,2);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,text,version,anamnesis_check_title) VALUES (8,3,'Darmende|Kopf|Fuss|Iris',2,'Woraus wurde Ihnen der Blinddarm entfernt?',0,2);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,text,version,anamnesis_check_title) VALUES (3,1,'',1,'Nehmen Sie zurzeit regelmässig Medikamente ein?',0,3);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,text,version,anamnesis_check_title) VALUES (4,2,'',0,'Welche Medikamente nehmen Sie ein?',0,3);
INSERT INTO `anamnesis_check` (id,sort_order,value,type,text,version,anamnesis_check_title) VALUES (7,3,'Prozac|Ritalin|Aspirin|Ethanol',3,'Nehmen Sie eines der aufgelisteten Medikamete und wenn ja, welche?',0,3);

INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (1,'0-0-1',NULL,NULL,0,2,2);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (7,NULL,NULL,0,0,3,2);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (8,NULL,NULL,1,0,1,3);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (9,NULL,NULL,0,0,3,3);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (10,NULL,NULL,1,0,1,4);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (11,'1-0-0',NULL,NULL,0,2,4);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (12,NULL,NULL,0,0,5,5);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (13,NULL,NULL,1,0,6,5);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (14,NULL,NULL,0,0,1,6);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (15,NULL,NULL,0,0,5,6);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (16,NULL,NULL,1,0,3,7);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (17,'Zygtoban,Bascurol',NULL,0,0,4,7);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (18,NULL,NULL,1,0,3,8);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (19,'Zygtoban',NULL,0,0,4,8);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (20,NULL,NULL,0,0,3,9);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (21,NULL,NULL,1,0,6,9);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (22,NULL,NULL,1,0,1,10);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (23,'0-1-0',NULL,0,0,2,10);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (24,NULL,NULL,1,0,3,11);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (25,'Aceton, Araldit',NULL,1,0,4,11);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (26,NULL,NULL,0,0,1,12);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (27,NULL,NULL,0,0,5,12);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (28,'1-0-0-1-0',NULL,NULL,0,9,11);
INSERT INTO `anamnesis_checks_value` (id,anamnesis_checks_value,comment,truth,version,anamnesischeck,anamnesisform) VALUES (29,'1-0-0-0',NULL,NULL,0,8,11);

INSERT INTO `bankaccount` (id,bic,iban,bank_name,version,country) VALUES (2,'0123','CH00 0000 0000 000 000','UBS AG',0,5);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (20,'GENODEF1JEV','CH46 3948 4853 2029 3','Basler Kantonalbank',0);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (29,'MIGRCHZZ80A','CH89 3940 4013 9483 3','Migros bank',0);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (30,'GENODEF1JEV','CH49 9494 9587 9387 0','BLKB',0);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (31,'BENDSFF1JEV','CH78 5685 7565 4364 7','KTS',0);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (32,'VTGODEF4EVODEF1JEV','CH76 8736 3893 3937 9','BBHG',0);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (33,'GVBDEF6ZHEV','CH28 9383 4847 3028 7','BW&S',0);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (34,'ZRWEODE4GEV','CH29 8474 9038 8933 4','Siga Bank',0);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (35,'BTZODEF3HEV','DE39 8476 4987 2987 8','Bank Auer',0);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version, country) VALUES (36,'UERNODEC4EV','DE39 2811 2904 4343','Commerzbank',0, 5);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (37,'QWNODEG0EV','FR88 9388 3022 2393 12','Banque Telex',0);
INSERT INTO `bankaccount` (id,bic,iban,bank_name,version) VALUES (38,'MKLODEF8FDV','FR93 2939 2938 9383 30','BBdP',0);

INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (19,'1987-07-04 00:00:00','Basel','dkohler@dkohler.ch',0,184,'076 441 03 44','Kohler',4051,'Daniel','Steinenbachgässlein 7',NULL,NULL,0,80,2,2,NULL,5,2);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (20,'1954-08-25 00:00:00','Metz','m.lamarie@unibas.ch',1,162,'078 427 24 85','Lamarie',4057,'Marianne','Feldbergstrasse 145',NULL,NULL,0,57,3,20,NULL,7,3);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (21,'1980-01-02 00:00:00','Liestal','pruggi@gmx.ch',0,197,'077 463 23 22','Preussler',4410,'Ferdinand','Kronenweg 23',NULL,NULL,0,140,4,29,NULL,5,5);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (22,'1955-03-12 00:00:00','Basel','karl.meyer@pps.ch',0,155,'077 529 57 25','Meyer',4056,'Karl','Karottenweg 4B',NULL,NULL,0,67,5,30,NULL,5,4);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (23,'1965-09-24 00:00:00','Basel','beddebu@hss.ch',1,182,'078 586 29 84','Buser',4051,'Bettina','Rankenbergweg 1',NULL,NULL,0,82,6,31,NULL,6,6);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (24,'1978-12-11 00:00:00','Muttenz','carla.joanna.v@yehi.ch',1,160,'076 352 28 44','Velazquez',4132,'Carla Joanna','Friedenstrasse 54',NULL,NULL,0,58,7,32,NULL,6,2);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (25,'1988-05-03 00:00:00','Allschwil','max.peter@calleymar.ch',0,159,'079 240 54 84','Peter',4123,'Max','Bröckelstrasse 11',NULL,NULL,0,72,8,33,NULL,5,2);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (26,'1944-06-19 00:00:00','Riehen','rumu@gallwers.ch',1,174,'079 258 05 18','Musyl',4125,'Ruth','Schiffstauankergasse 10',NULL,NULL,0,58,9,34,NULL,5,8);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (27,'1955-10-10 00:00:00','Lörrach','ljiljana.ivanovic@gerber.de',1,165,'077 548 46 18','Ivanovic',79539,'Ljiljana','Baslerstrasse 44',NULL,NULL,0,57,10,35,NULL,6,9);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (28,'1974-11-08 00:00:00','Brombach','Konradenauer@der.de',0,182,'075 456 12 78','Adenauer',69434,'Benjamin','Himbacherweg 99',NULL,NULL,0,90,11,36,NULL,6,2);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (29,'1980-04-02 00:00:00','Mulhouse','delphy@sirasse.fr',1,162,'079 456 18 47','Landwerlin',68200,'Delphine','Route du Chemin 13',NULL,NULL,0,69,12,37,NULL,7,7);
INSERT INTO `standardized_patient` (id,birthday,city,email,gender,height,mobile,name,postal_code,pre_name,street,telephone,telephone2,version,weight,anamnesis_form,bank_account,descriptions,nationality,profession) VALUES (30,'1950-08-29 00:00:00','St. Louis','ca.co@ebe.fr',0,188,'078 471 15 45','Collars',97450,'Carl','Avenue des Arbres 231',NULL,NULL,0,95,13,38,NULL,7,2);

INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (6,6,0,4,19);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (7,4,0,6,19);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (8,6,0,4,20);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (9,6,0,4,21);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (10,2,0,8,21);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (11,6,0,4,22);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (12,6,0,4,23);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (13,6,0,4,24);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (14,6,0,4,25);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (15,6,0,4,26);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (16,6,0,4,27);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (17,6,0,4,28);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (18,4,0,7,28);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (19,6,0,5,29);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (20,5,0,4,29);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (21,6,0,5,30);
INSERT INTO `lang_skill` (id,skill,version,spokenlanguage,standardizedpatient) VALUES (22,3,0,4,30);

INSERT INTO `scar` (id,bodypart,trait_type,version) VALUES (9,'Oberschenkel (links)',0,0);
INSERT INTO `scar` (id,bodypart,trait_type,version) VALUES (10,'Schulterblatt (Links)',0,0);
INSERT INTO `scar` (id,bodypart,trait_type,version) VALUES (11,'Hals',0,0);
INSERT INTO `scar` (id,bodypart,trait_type,version) VALUES (12,'Oberarm (Rechts)',1,0);
INSERT INTO `scar` (id,bodypart,trait_type,version) VALUES (13,'Wade (Links)',1,0);
INSERT INTO `scar` (id,bodypart,trait_type,version) VALUES (15,'Rechtes Schulterblatt',2,0);

INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (2,9);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (3,10);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (4,12);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (4,13);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (5,15);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (6,11);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (7,10);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (8,9);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (9,9);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (10,12);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (11,12);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (12,13);
INSERT INTO `anamnesis_form_scars` (anamnesis_forms,scars) VALUES (13,15);

INSERT INTO `room` (room_number, length, width, version) VALUES ('LZM - 101', '5.0', '4.3', 0);
INSERT INTO `room` (room_number, length, width, version) VALUES ('LZM - 102', '3.4', '4.1', 0);
INSERT INTO `room` (room_number, length, width, version) VALUES ('LZM - 103', '2.3', '6.3', 0);
INSERT INTO `room` (room_number, length, width, version) VALUES ('LZM - 104', '6.4', '7.5', 0);

INSERT INTO `log_entry` (logtime, new_value, old_value, shib_id, version) VALUES (NOW(), 'neuer Wert1', 'alter Wert1', '012345', 0);
INSERT INTO `log_entry` (logtime, new_value, old_value, shib_id, version) VALUES (NOW(), 'neuer Wert2', 'alter Wert2', '012345', 0);
INSERT INTO `log_entry` (logtime, new_value, old_value, shib_id, version) VALUES (NOW(), 'neuer Wert3', 'alter Wert3', '543210', 0);
INSERT INTO `log_entry` (logtime, new_value, old_value, shib_id, version) VALUES (NOW(), 'neuer Wert4', 'alter Wert4', '543210', 0);
INSERT INTO `log_entry` (logtime, new_value, old_value, shib_id, version) VALUES (NOW(), 'neuer Wert5', 'alter Wert5', '012345', 0);



INSERT INTO `specialisation` (name, version) VALUES ('Ear-Nose-Throat', 0);
INSERT INTO `specialisation` (name, version) VALUES ('Arms', 0);
INSERT INTO `specialisation` (name, version) VALUES ('Legs', 0);
INSERT INTO `specialisation` (name, version) VALUES ('Sewing', 0);

INSERT INTO `role_topic` (name, description, study_year, slots_until_change, specialisation, version) VALUES ('HNO', 'Ear-Nose-Throat', 2, 10, 1, 0);
INSERT INTO `role_topic` (name, description, study_year, slots_until_change, specialisation, version) VALUES ('Arm', 'Arm Problems', 2, 10, 2, 0);
INSERT INTO `role_topic` (name, description, study_year, slots_until_change, specialisation, version) VALUES ('Knie', 'Knee Problem', 2, 0, 3, 0);
INSERT INTO `role_topic` (name, description, study_year, slots_until_change, specialisation, version) VALUES ('Naht', 'Stiching up sth.', 2, 0, 4, 0);

INSERT INTO `standardized_role` (role_topic, short_name, long_name, version, active, role_type, study_year) VALUES (1, 'HNO - A', 'HNO - Version A', 0, 1, 0, 3);
INSERT INTO `standardized_role` (role_topic, short_name, long_name, version, active, role_type, study_year) VALUES (2, 'Arm - A', 'Arm - Version A', 0, 1, 1, 3);
INSERT INTO `standardized_role` (role_topic, short_name, long_name, version, active, role_type, study_year) VALUES (3, 'Knie - A', 'Knie - Version A', 0, 1, 2, 3);
INSERT INTO `standardized_role` (role_topic, short_name, long_name, version, active, role_type, study_year) VALUES (4, 'Naht - A', 'Naht - Version A', 0, 1, 0, 3);
INSERT INTO `standardized_role` (role_topic, short_name, long_name, version, active, role_type, study_year) VALUES (1, 'HNO - B', 'HNO - Version B', 0, 1, 1, 3);
INSERT INTO `standardized_role` (role_topic, short_name, long_name, version, active, role_type, study_year) VALUES (2, 'Arm - B', 'Arm - Version B', 0, 1, 2, 3);
INSERT INTO `standardized_role` (role_topic, short_name, long_name, version, active, role_type, study_year) VALUES (3, 'Knie - B', 'Knie - Version B', 0, 0, 0, 3);
INSERT INTO `standardized_role` (role_topic, short_name, long_name, version, active, role_type, study_year) VALUES (4, 'Naht - B', 'Naht - Version B', 0, 1, 1, 3);

INSERT INTO `osce` (is_repe_osce, is_valid, max_number_students, number_courses, number_rooms, post_length, short_break, middle_break, long_break, study_year, name, osce_status, semester, version) VALUES (0, 1, 130, 0, 16, 13, 1, 15, 35, 3, 'Test 1', 2, 1, 0);
INSERT INTO `osce` (is_repe_osce, is_valid, max_number_students, number_courses, number_rooms, post_length, short_break, middle_break, long_break, study_year, name, osce_status, semester, version) VALUES (0, 1, 120, 0, 16, 20, 1, 20, 45, 3, 'Test 2', 2, 1, 0);

INSERT INTO `osce_day` (osce_date, time_start, time_end, osce, version) VALUES('2012-06-18', '2012-06-18 09:00', '2012-06-18 18:00', 1, 0);
INSERT INTO `osce_day` (osce_date, time_start, time_end, osce, version) VALUES('2012-06-19', '2012-06-18 09:00', '2012-06-18 17:00', 2, 0);
INSERT INTO `osce_day` (osce_date, time_start, time_end, osce, version) VALUES('2012-06-20', '2012-06-18 09:00', '2012-06-18 11:00', 2, 0);

INSERT INTO `osce_post_blueprint` (is_possible_start, sequence_number, role_topic, osce, post_type, specialisation, version) VALUES (1, 1, 1, 1, 0, 1, 0);
INSERT INTO `osce_post_blueprint` (is_possible_start, sequence_number, role_topic, osce, post_type, specialisation, version) VALUES (1, 2, 2, 1, 0, 2, 0);
INSERT INTO `osce_post_blueprint` (is_possible_start, sequence_number, role_topic, osce, post_type, specialisation, version) VALUES (1, 3, 3, 1, 0, 3, 0);
INSERT INTO `osce_post_blueprint` (is_possible_start, sequence_number, role_topic, osce, post_type, specialisation, version) VALUES (1, 4, 4, 1, 0, 4, 0);
INSERT INTO `osce_post_blueprint` (is_possible_start, sequence_number, role_topic, osce, post_type, specialisation, version) VALUES (1, 1, 1, 2, 2, 1, 0);
INSERT INTO `osce_post_blueprint` (is_possible_start, sequence_number, role_topic, osce, post_type, specialisation, version) VALUES (0, 2, 2, 2, 3, 2, 0);
INSERT INTO `osce_post_blueprint` (is_possible_start, sequence_number, role_topic, osce, post_type, specialisation, version) VALUES (1, 3, 3, 2, 0, 3, 0);
INSERT INTO `osce_post_blueprint` (is_possible_start, sequence_number, role_topic, osce, post_type, specialisation, version) VALUES (1, 4, 4, 2, 0, 4, 0);

INSERT INTO `osce_sequence` (label, number_rotation, osce_day, version) VALUES ('A', 4, 1, 0);
INSERT INTO `osce_sequence` (label, number_rotation, osce_day, version) VALUES ('B', 4, 1, 0);
INSERT INTO `osce_sequence` (label, number_rotation, osce_day, version) VALUES ('A', 3, 2, 0);
INSERT INTO `osce_sequence` (label, number_rotation, osce_day, version) VALUES ('B', 3, 2, 0);
INSERT INTO `osce_sequence` (label, number_rotation, osce_day, version) VALUES ('C', 2, 2, 0);

INSERT INTO `course` (color, osce_sequence, version) VALUES ('blue', 1, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('red', 1, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('green', 1, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('yellow', 1, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('blue', 2, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('red', 2, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('green', 2, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('yellow', 2, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('blue', 3, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('red', 3, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('green', 3, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('blue', 4, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('red', 4, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('green', 4, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('blue', 5, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('red', 5, 0);
INSERT INTO `course` (color, osce_sequence, version) VALUES ('green', 5, 0);

INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (1, 1, 1, 1);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (1, 2, 2, 2);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (1, 3, 3, 3);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (1, 4, 4, 4);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (2, 1, 1, 5);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (2, 2, 2, 6);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (2, 3, 3, 7);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (2, 4, 4, 8);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 1, 5, 1);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 2, 6, 2);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 3, 7, 3);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 4, 8, 4);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 1, 5, 5);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 2, 6, 6);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 3, 7, 7);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 4, 8, 8);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 1, 5, null);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 2, 6, null);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 3, 7, null);
INSERT INTO `osce_post` (osce_sequence, sequence_number, osce_post_blueprint, standardized_role) VALUES (3, 4, 8, null);
