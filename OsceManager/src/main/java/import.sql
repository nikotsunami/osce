INSERT INTO `osce`.`administrator` (`email`, `name`, `pre_name`, `version`) VALUES ('foo@bar.com', 'Bar', 'Foo', '0');
INSERT INTO `osce`.`administrator` (`email`, `name`, `pre_name`, `version`) VALUES ('bar@foo.com', 'Foo', 'Bar', '0');
INSERT INTO `osce`.`administrator` (`email`, `name`, `pre_name`, `version`) VALUES ('hansruedi.meier@gmail.com', 'Meier', 'Hansruedi', '0');

INSERT INTO `osce`.`semester` (`semester`, `cal_year`, `version`) VALUES ('0', '2011', '0');

INSERT INTO `osce`.`administrator_semesters` (`administrators`, `semesters`) VALUES ('1', '1');
INSERT INTO `osce`.`administrator_semesters` (`administrators`, `semesters`) VALUES ('2', '1');

INSERT INTO `osce`.`nationality` (`nationality`, `version`) VALUES ('Schweiz', '0');
INSERT INTO `osce`.`nationality` (`nationality`, `version`) VALUES ('Deutschland', '0');
INSERT INTO `osce`.`nationality` (`nationality`, `version`) VALUES ('Frankreich', '0');
INSERT INTO `osce`.`nationality` (`nationality`, `version`) VALUES ('Japan', '0');

INSERT INTO `osce`.`clinic` (`city`, `name`, `postal_code`, `street`, `version`) VALUES ('Basel', 'UKBB xx', '4000', 'UKBB-Strasse 1', '0');
INSERT INTO `osce`.`clinic` (`city`, `name`, `postal_code`, `street`, `version`) VALUES ('Liestal', 'Kantonsspital xx', '1', 'Hauptstrasse x', '0');

INSERT INTO `osce`.`office` (`email`, `gender`, `name`, `pre_name`, `telephone`, `title`, `version`) VALUES ('bar@foo.com', '1', 'Office', 'Officer', '987654321', '1', '0');
INSERT INTO `osce`.`office` (`email`, `gender`, `name`, `pre_name`, `telephone`, `title`, `version`) VALUES ('foo@bar.com', '1', 'Blah', 'Blubb', '123456789', '1', '0');

INSERT INTO `osce`.`doctor` (`email`, `gender`, `name`, `pre_name`, `telephone`, `title`, `version`, `office`, `clinic`) VALUES ('dr.who@who.com', '1', 'Who', 'Doctor', '123456', 'Dr. Med. Dent.', '0', '1', '2');
INSERT INTO `osce`.`doctor` (`email`, `gender`, `name`, `pre_name`, `telephone`, `title`, `version`, `office`, `clinic`) VALUES ('dr.house@house.com', '1', 'House', 'Gregory', '123456', 'Dr.', '0', '2', '1');

INSERT INTO `osce`.`profession` (`profession`, `version`) VALUES ('Programmierer', '0');

INSERT INTO `osce`.`spoken_language` (`language_name`, `version`) VALUES ('Deutsch', '0');
INSERT INTO `osce`.`spoken_language` (`language_name`, `version`) VALUES ('Englisch', '0');
INSERT INTO `osce`.`spoken_language` (`language_name`, `version`) VALUES ('Tschechisch', '0');

INSERT INTO `osce`.`anamnesis_form` (`create_date`, `version`) VALUES ('2011-03-12', '0');

INSERT INTO `osce`.`bankaccount` (`bic`, `iban`, `bank_name`, `version`) VALUES ('1234', 'CH12 3456 7890 1234 ABC', 'UBS', '0');

INSERT INTO `osce`.`description` (`description`, `version`) VALUES ('sample description', '0');

INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);
INSERT INTO `osce`.`standardized_patient` (`birthday`, `city`, `email`, `gender`, `mobile`, `name`, `postal_code`, `pre_name`, `street`, `telephone`, `version`, `nationality`, `profession`, `bank_account`, `descriptions`, `anamnesis_form`) VALUES ('2010-10-10', 'Basel', 'foo@bar.com', '1', '123456789', 'Bar', '4000', 'Foo', 'Samplestreet 42', '123456789', '0', '4', '1', '1', '1', 1);

INSERT INTO `osce`.`lang_skill` (`skill`, `spokenlanguage`, `standardizedpatient`, `version`) VALUES (1, '1', '1', '0');
INSERT INTO `osce`.`lang_skill` (`skill`, `spokenlanguage`, `standardizedpatient`, `version`) VALUES (2, '3', '1', '0');

INSERT INTO `osce`.`scar` (`bodypart`, `trait_type`, `version`) VALUES ('Oberarm (links)', '0', '0');
INSERT INTO `osce`.`scar` (`bodypart`, `trait_type`, `version`) VALUES ('Oberarm (rechts)', '0', '0');
INSERT INTO `osce`.`scar` (`bodypart`, `trait_type`, `version`) VALUES ('Unterarm (links)', '0', '0');
INSERT INTO `osce`.`scar` (`bodypart`, `trait_type`, `version`) VALUES ('Unterarm (rechts)', '0', '0');
INSERT INTO `osce`.`scar` (`bodypart`, `trait_type`, `version`) VALUES ('Knie (links)', '0', '0');
INSERT INTO `osce`.`scar` (`bodypart`, `trait_type`, `version`) VALUES ('Knie (rechts)', '0', '0');

INSERT INTO `osce`.`anamnesis_form_scars` (`anamnesis_forms`, `scars`) VALUES (1, 1);
INSERT INTO `osce`.`anamnesis_form_scars` (`anamnesis_forms`, `scars`) VALUES (1, 3);
