INSERT INTO User(id, fbId, gplusId, firstname, secondname, email, login, passwordHash, birthdate, sex, hasAvatar, preDefined) VALUES (1,  '1', '1', 'Andrey', 'Markhel', 'amarkhel@exadel.com',  'amarkhel',       '8cb2237d0679ca88db6464eac60da96345513964',  '1985-01-08', 0, false, true);
INSERT INTO User(id, fbId, gplusId, firstname, secondname, email, login, passwordHash, birthdate, sex, hasAvatar, preDefined) VALUES (2,  '1', '1', 'Nick',   'Curtis',  'nkurtis@iba.com',      'Viking',         '8cb2237d0679ca88db6464eac60da96345513964',  '1978-01-08', 1, false, true);
INSERT INTO User(id, fbId, gplusId, firstname, secondname, email, login, passwordHash, birthdate, sex, hasAvatar, preDefined) VALUES (3,  '1', '1', 'John',   'Smith',   'jsmith@jboss.com',     'Noname',         '8cb2237d0679ca88db6464eac60da96345513964',  '1970-01-08', 1, false, true);

INSERT INTO User(id, fbId, gplusId, firstname, secondname, email, login, passwordHash, birthdate, sex, hasAvatar, preDefined) VALUES (10, '1', '1', 'John',   'Smith',   'jsmith_10@jboss.com',     'user_for_add',   '8cb2237d0679ca88db6464eac60da96345513964',  '1970-01-08', 1, false, false);
INSERT INTO User(id, fbId, gplusId, firstname, secondname, email, login, passwordHash, birthdate, sex, hasAvatar, preDefined) VALUES (11, '1', '1', 'John',   'Smith',   'jsmith_11@jboss.com',     'user_for_del',   '8cb2237d0679ca88db6464eac60da96345513964',  '1970-01-08', 1, false, false);
INSERT INTO User(id, fbId, gplusId, firstname, secondname, email, login, passwordHash, birthdate, sex, hasAvatar, preDefined) VALUES (12, '1', '1', 'John',   'Smith',   'jsmith_12@jboss.com',     'user_for_dnd',  '8cb2237d0679ca88db6464eac60da96345513964',  '1970-01-08', 1, false, false);

INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (1, 'Nature',       'Nature pictures',        1, '2009-12-18', true);
INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (2, 'Sport & Cars', 'Sport & Cars pictures',  1, '2009-12-18', true);
INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (3, 'Portrait',     'Human faces',            2, '2009-12-18', true);
INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (4, 'Monuments',    'Monuments pictures',     3, '2009-12-18', true);
INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (5, 'Water',        'Water pictures',         3, '2009-12-18', true);

INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (100, 'MyShelf 100',     'MyShelf',               10, '2009-12-18', false);
INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (101, 'MyShelf 101',     'MyShelf',               10, '2009-12-18', false);
INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (110, 'MyShelf 110',     'MyShelf',               11, '2009-12-18', false);
INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (111, 'MyShelf 111',     'MyShelf',               11, '2009-12-18', false);
INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (120, 'MyShelf 120',     'MyShelf',               12, '2009-12-18', false);
INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (121, 'MyShelf 121',     'MyShelf',               12, '2009-12-18', false);

---------------------------------------------------------------------
-- ALBUM - Animals"
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (0, 'Animals', 'Animals pictures',  1, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (0,  'Animals');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (0, '1750979205_6e51b47ce9_o.jpg', '1750979205_6e51b47ce9_o.jpg', 'Animals - 1750979205_6e51b47ce9_o.jpg image',  '2009-12-18', 0, 'Canon EOS 450D', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 0);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (1, '1906662004_655d0f6ccf_o.jpg', '1906662004_655d0f6ccf_o.jpg', 'Animals - 1906662004_655d0f6ccf_o.jpg image',  '2009-12-18', 0, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (2, '2090459727_f2888e5cbe_o.jpg', '2090459727_f2888e5cbe_o.jpg', 'Animals - 2090459727_f2888e5cbe_o.jpg image',  '2009-12-18', 0, 'Canon PowerShot SX110 IS', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (0, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',2, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (1, '1985-01-08', 'Gorgeous! Lovely color!',2, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (2, '1985-01-08', 'Gorgeous! Lovely color!',2, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (3, '2297752925_de29b5fb10_o.jpg', '2297752925_de29b5fb10_o.jpg', 'Animals - 2297752925_de29b5fb10_o.jpg image',  '2009-12-18', 0, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (3, '1985-01-08', 'Superb Shot and so beautiful Colors !!! ',3, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (4, '1985-01-08', 'whoah ! wonderful',3, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (5, '1985-01-08', 'Such a lovely colour azaga!',3, 3);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (4, '2298556444_2151b7a6c4_o.jpg', '2298556444_2151b7a6c4_o.jpg', 'Animals - 2298556444_2151b7a6c4_o.jpg image',  '2009-12-18', 0, 'Sony Alpha DSLR-A350', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 4);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (6, '1985-01-08', 'Beautiful colours. Nice close up. ',4, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (7, '1985-01-08', 'Amazing shot..',4, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (8, '1985-01-08', '++Beautiful',4, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (9, '1985-01-08', 'Beautiful colours. Nice close up. ',4, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (5, '2508246015_313952406c_o.jpg', '2508246015_313952406c_o.jpg', 'Animals - 2508246015_313952406c_o.jpg image',  '2009-12-18', 0, 'Canon Digital IXUS 80 IS (PowerShot SD1100 IS)', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 5);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (10, '1985-01-08', 'Perfecft!',5, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (11, '1985-01-08', 'whoah ! wonderful',5, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (12, '1985-01-08', 'Gorgeous! Lovely color!',5, 3);
UPDATE Album set coveringImage_id=5 where id = 0;

---------------------------------------------------------------------
-- ALBUM - Cars"
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (1, 'Cars', 'Cars pictures',  2, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (1,  'Cars');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (6, '190193308_ce2a4de5fa_o.jpg', '190193308_ce2a4de5fa_o.jpg', 'Cars - 190193308_ce2a4de5fa_o.jpg image',  '2009-12-18', 1, 'Pentax Optio E40', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 6);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (7, '1941230817_bcce17b8ef_o.jpg', '1941230817_bcce17b8ef_o.jpg', 'Cars - 1941230817_bcce17b8ef_o.jpg image',  '2009-12-18', 1, 'Olympus Stylus mju 1040', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 7);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (13, '1985-01-08', 'love every thing about this picture, really beautiful... :))',7, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (14, '1985-01-08', 'Very *lovely*',7, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (15, '1985-01-08', 'Very *lovely*',7, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (16, '1985-01-08', 'Gorgeous! Lovely color!',7, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (8, '2151423750_129317a034_o.jpg', '2151423750_129317a034_o.jpg', 'Cars - 2151423750_129317a034_o.jpg image',  '2009-12-18', 1, 'BBK DP810', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 8);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (17, '1985-01-08', 'Superb Shot and so beautiful Colors !!! ',8, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (9, '2233985073_9a3fd7d3ac_b.jpg', '2233985073_9a3fd7d3ac_b.jpg', 'Cars - 2233985073_9a3fd7d3ac_b.jpg image',  '2009-12-18', 1, 'BenQ DC E800', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 9);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (18, '1985-01-08', 'Superb Shot and so beautiful Colors !!! ',9, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (19, '1985-01-08', 'Bellissima macro!',9, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (10, '2386071696_2b4e84eddb_o.jpg', '2386071696_2b4e84eddb_o.jpg', 'Cars - 2386071696_2b4e84eddb_o.jpg image',  '2009-12-18', 1, 'Konica Minolta', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 10);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (20, '1985-01-08', 'Such a lovely colour azaga!',10, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (11, '3089719367_a03a2b55a4_b.jpg', '3089719367_a03a2b55a4_b.jpg', 'Cars - 3089719367_a03a2b55a4_b.jpg image',  '2009-12-18', 1, 'Panasonic', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 11);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (21, '1985-01-08', 'Very *lovely*',11, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (22, '1985-01-08', 'whoah ! wonderful',11, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (23, '1985-01-08', 'Fantastic job. Great light and color! Great shot!',11, 2);
UPDATE Album set coveringImage_id=11 where id = 1;

---------------------------------------------------------------------
-- ALBUM - Monuments and just buildings"
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (2, 'Monuments and just buildings', 'Monuments and just buildings pictures',  4, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (2,  'Monuments and just buildings');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (12, '05[303x457].jpg', '05[303x457].jpg', 'Monuments and just buildings - 05[303x457].jpg image',  '2009-12-18', 2, 'LG LDC-A310', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(2, 12);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (24, '1985-01-08', 'Superb Shot and so beautiful Colors !!! ',12, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (25, '1985-01-08', 'really pretty. it looks like there is a lady in the _center_, blowing kisses!!',12, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (26, '1985-01-08', 'that is a beautiful flower with great colours ',12, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (13, '07[303x457].jpg', '07[303x457].jpg', 'Monuments and just buildings - 07[303x457].jpg image',  '2009-12-18', 2, 'Canon EOS 450D', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(2, 13);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (27, '1985-01-08', 'Wonderful.',13, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (28, '1985-01-08', 'really pretty. it looks like there is a lady in the _center_, blowing kisses!!',13, 3);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (14, '1805365000_ca64d20b10_o.jpg', '1805365000_ca64d20b10_o.jpg', 'Monuments and just buildings - 1805365000_ca64d20b10_o.jpg image',  '2009-12-18', 2, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(2, 14);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (29, '1985-01-08', 'Stunning capture! :-)',14, 1);
UPDATE Album set coveringImage_id=14 where id = 2;

---------------------------------------------------------------------
-- ALBUM - Nature"
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (3, 'Nature', 'Nature pictures',  1, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (3,  'Nature');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (15, '01[303x202].jpg', '01[303x202].jpg', 'Nature - 01[303x202].jpg image',  '2009-12-18', 3, 'Canon PowerShot SX110 IS', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(3, 15);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (16, '1[305x457].jpg', '1[305x457].jpg', 'Nature - 1[305x457].jpg image',  '2009-12-18', 3, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(3, 16);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (30, '1985-01-08', 'Bellísima.!!! saludos.',16, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (31, '1985-01-08', 'Amazing shot..',16, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (32, '1985-01-08', 'Gorgeous! Lovely color!',16, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (33, '1985-01-08', 'Very *lovely*',16, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (17, '273927725_c9f5ef5952_o.jpg', '273927725_c9f5ef5952_o.jpg', 'Nature - 273927725_c9f5ef5952_o.jpg image',  '2009-12-18', 3, 'Sony Alpha DSLR-A350', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(3, 17);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (34, '1985-01-08', 'Wow!! Macro stupenda!!! Complimenti! ',17, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (35, '1985-01-08', 'Bellísima.!!! saludos.',17, 3);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (18, '2[303x457].jpg', '2[303x457].jpg', 'Nature - 2[303x457].jpg image',  '2009-12-18', 3, 'Canon Digital IXUS 80 IS (PowerShot SD1100 IS)', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(3, 18);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (36, '1985-01-08', 'this is extremely Good:) Congratulations!',18, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (37, '1985-01-08', 'whoah ! wonderful',18, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (19, '3392730627_1cdb18cba6_o.jpg', '3392730627_1cdb18cba6_o.jpg', 'Nature - 3392730627_1cdb18cba6_o.jpg image',  '2009-12-18', 3, 'Pentax Optio E40', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(3, 19);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (20, '3392993334_36d7f097df_o.jpg', '3392993334_36d7f097df_o.jpg', 'Nature - 3392993334_36d7f097df_o.jpg image',  '2009-12-18', 3, 'Olympus Stylus mju 1040', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(3, 20);
UPDATE Album set coveringImage_id=20 where id = 3;

---------------------------------------------------------------------
-- ALBUM - Portrait"
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (4, 'Portrait', 'Portrait pictures',  3, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (4,  'Portrait');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (21, '02[303x202].jpg', '02[303x202].jpg', 'Portrait - 02[303x202].jpg image',  '2009-12-18', 4, 'BBK DP810', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(4, 21);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (38, '1985-01-08', 'Bellissima macro!',21, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (39, '1985-01-08', 'Wow!! Macro stupenda!!! Complimenti! ',21, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (40, '1985-01-08', 'I Think this is Art!',21, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (41, '1985-01-08', 'this is extremely Good:) Congratulations!',21, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (22, '1033975999_7e058fcf1c_o.jpg', '1033975999_7e058fcf1c_o.jpg', 'Portrait - 1033975999_7e058fcf1c_o.jpg image',  '2009-12-18', 4, 'BenQ DC E800', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(4, 22);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (23, '1516027705_ddff0a70dd_o.jpg', '1516027705_ddff0a70dd_o.jpg', 'Portrait - 1516027705_ddff0a70dd_o.jpg image',  '2009-12-18', 4, 'Konica Minolta', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(4, 23);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (42, '1985-01-08', 'Superb Shot and so beautiful Colors !!! ',23, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (43, '1985-01-08', 'Wow!! Macro stupenda!!! Complimenti! ',23, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (44, '1985-01-08', 'Beautiful ^Flower^...great Macro....Excellent !!!',23, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (45, '1985-01-08', 'Amazing shot..',23, 1);
UPDATE Album set coveringImage_id=23 where id = 4;

---------------------------------------------------------------------
-- ALBUM - Sport"
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (5, 'Sport', 'Sport pictures',  2, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (5,  'Sport');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (24, '103193233_860c47c909_o.jpg', '103193233_860c47c909_o.jpg', 'Sport - 103193233_860c47c909_o.jpg image',  '2009-12-18', 5, 'Panasonic', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(5, 24);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (46, '1985-01-08', 'Bellissima macro!',24, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (25, '1350250361_2d963dd4e7_o.jpg', '1350250361_2d963dd4e7_o.jpg', 'Sport - 1350250361_2d963dd4e7_o.jpg image',  '2009-12-18', 5, 'LG LDC-A310', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(5, 25);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (47, '1985-01-08', 'Perfecft!',25, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (48, '1985-01-08', 'Stunning capture! :-)',25, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (49, '1985-01-08', 'really pretty. it looks like there is a lady in the _center_, blowing kisses!!',25, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (26, '2042654579_d25c0db64f_o.jpg', '2042654579_d25c0db64f_o.jpg', 'Sport - 2042654579_d25c0db64f_o.jpg image',  '2009-12-18', 5, 'Canon EOS 450D', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(5, 26);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (50, '1985-01-08', 'Beautiful colours. Nice close up. ',26, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (51, '1985-01-08', 'Perfecft!',26, 2);
UPDATE Album set coveringImage_id=26 where id = 5;

---------------------------------------------------------------------
-- ALBUM - Water"
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (6, 'Water', 'Water pictures',  5, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (6,  'Water');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (27, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 6, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(6, 27);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (52, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',27, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (53, '1985-01-08', 'love every thing about this picture, really beautiful... :))',27, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (54, '1985-01-08', 'Perfecft!',27, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (55, '1985-01-08', 'Beautiful colours. Nice close up. ',27, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (28, '1323769314_fe850cd954_o.jpg', '1323769314_fe850cd954_o.jpg', 'Water - 1323769314_fe850cd954_o.jpg image',  '2009-12-18', 6, 'Canon PowerShot SX110 IS', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(6, 28);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (56, '1985-01-08', 'Fantastic job. Great light and color! Great shot!',28, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (29, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 6, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(6, 29);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (30, '2198502835_1644c8fde2_o.jpg', '2198502835_1644c8fde2_o.jpg', 'Water - 2198502835_1644c8fde2_o.jpg image',  '2009-12-18', 6, 'Sony Alpha DSLR-A350', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(6, 30);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (57, '1985-01-08', 'Perfecft!',30, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (31, '2242254221_f2af58e243_o.jpg', '2242254221_f2af58e243_o.jpg', 'Water - 2242254221_f2af58e243_o.jpg image',  '2009-12-18', 6, 'Canon Digital IXUS 80 IS (PowerShot SD1100 IS)', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(6, 31);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (58, '1985-01-08', 'fantastic shot !!!!!!',31, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (59, '1985-01-08', 'Bellissima macro!',31, 3);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (32, '3170219697_4d259ff802_o.jpg', '3170219697_4d259ff802_o.jpg', 'Water - 3170219697_4d259ff802_o.jpg image',  '2009-12-18', 6, 'Pentax Optio E40', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(6, 32);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (60, '1985-01-08', 'I Think this is Art!',32, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (61, '1985-01-08', 'Fantastic job. Great light and color! Great shot!',32, 2);
UPDATE Album set coveringImage_id=32 where id = 6;


---------------------------------------------------------------------
---------------------------------------------------------------------
-- TEST DATA
---------------------------------------------------------------------
---------------------------------------------------------------------

---------------------------------------------------------------------
-- ALBUM - MyShelf - 100
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (100, 'MyAlbum 100', 'MyAlbum pictures',  100, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (10006,  'MyAlbum_100');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (100027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 100, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(10006, 100027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (100052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',100027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (100053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',100027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (100054, '1985-01-08', 'Perfecft!',100027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (100055, '1985-01-08', 'Beautiful colours. Nice close up. ',100027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (100029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 100, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(10006, 100029);
UPDATE Album set coveringImage_id=100027 where id = 1000;
---------------------------------------------------------------------
-- ALBUM - MyShelf - 101
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (101, 'MyAlbum 101', 'MyAlbum pictures',  100, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (10106,  'MyAlbum_101');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (101027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 101, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(10106, 101027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (101052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',101027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (101053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',101027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (101054, '1985-01-08', 'Perfecft!',101027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (101055, '1985-01-08', 'Beautiful colours. Nice close up. ',101027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (101029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 101, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(10106, 101029);
UPDATE Album set coveringImage_id=101027 where id = 1010;
---------------------------------------------------------------------
-- ALBUM - MyShelf - 102
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (102, 'MyAlbum 102', 'MyAlbum pictures',  101, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (10206,  'MyAlbum_102');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (102027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 102, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(10206, 102027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (102052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',102027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (102053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',102027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (102054, '1985-01-08', 'Perfecft!',102027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (102055, '1985-01-08', 'Beautiful colours. Nice close up. ',102027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (102029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 102, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(10206, 102029);
UPDATE Album set coveringImage_id=102027 where id = 1020;
---------------------------------------------------------------------
-- ALBUM - MyShelf - 103
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (103, 'MyAlbum 103', 'MyAlbum pictures',  101, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (10306,  'MyAlbum_103');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (103027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 103, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(10306, 103027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (103052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',103027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (103053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',103027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (103054, '1985-01-08', 'Perfecft!',103027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (103055, '1985-01-08', 'Beautiful colours. Nice close up. ',103027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (103029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 103, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(10306, 103029);
UPDATE Album set coveringImage_id=103027 where id = 1030;

---------------------------------------------------------------------
-- ALBUM - MyShelf - 110
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (110, 'MyAlbum 110', 'MyAlbum pictures',  110, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (11006,  'MyAlbum_110');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (110027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 110, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(11006, 110027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (110052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',110027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (110053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',110027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (110054, '1985-01-08', 'Perfecft!',110027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (110055, '1985-01-08', 'Beautiful colours. Nice close up. ',110027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (110029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 110, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(11006, 110029);
UPDATE Album set coveringImage_id=110027 where id = 1100;
---------------------------------------------------------------------
-- ALBUM - MyShelf - 111
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (111, 'MyAlbum 111', 'MyAlbum pictures',  110, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (11106,  'MyAlbum_111');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (111027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 111, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(11106, 111027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (111052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',111027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (111053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',111027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (111054, '1985-01-08', 'Perfecft!',111027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (111055, '1985-01-08', 'Beautiful colours. Nice close up. ',111027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (111029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 111, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(11106, 111029);
UPDATE Album set coveringImage_id=111027 where id = 1110;
---------------------------------------------------------------------
-- ALBUM - MyShelf - 112
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (112, 'MyAlbum 112', 'MyAlbum pictures',  111, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (11206,  'MyAlbum_112');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (112027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 112, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(11206, 112027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (112052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',112027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (112053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',112027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (112054, '1985-01-08', 'Perfecft!',112027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (112055, '1985-01-08', 'Beautiful colours. Nice close up. ',112027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (112029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 112, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(11206, 112029);
UPDATE Album set coveringImage_id=112027 where id = 1120;
---------------------------------------------------------------------
-- ALBUM - MyShelf - 113
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (113, 'MyAlbum 113', 'MyAlbum pictures',  111, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (11306,  'MyAlbum_113');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (113027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 113, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(11306, 113027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (113052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',113027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (113053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',113027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (113054, '1985-01-08', 'Perfecft!',113027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (113055, '1985-01-08', 'Beautiful colours. Nice close up. ',113027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (113029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 113, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(11306, 113029);
UPDATE Album set coveringImage_id=113027 where id = 1130;

---------------------------------------------------------------------
-- ALBUM - MyShelf - 120
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (120, 'MyAlbum 120', 'MyAlbum pictures',  120, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (12006,  'MyAlbum_120');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (120027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 120, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(12006, 120027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (120052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',120027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (120053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',120027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (120054, '1985-01-08', 'Perfecft!',120027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (120055, '1985-01-08', 'Beautiful colours. Nice close up. ',120027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (120029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 120, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(12006, 120029);
UPDATE Album set coveringImage_id=120027 where id = 1200;
---------------------------------------------------------------------
-- ALBUM - MyShelf - 121
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (121, 'MyAlbum 121', 'MyAlbum pictures',  120, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (12106,  'MyAlbum_121');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (121027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 121, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(12106, 121027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (121052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',121027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (121053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',121027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (121054, '1985-01-08', 'Perfecft!',121027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (121055, '1985-01-08', 'Beautiful colours. Nice close up. ',121027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (121029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 121, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(12106, 121029);
UPDATE Album set coveringImage_id=121027 where id = 1210;
---------------------------------------------------------------------
-- ALBUM - MyShelf - 122
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (122, 'MyAlbum 122', 'MyAlbum pictures',  121, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (12206,  'MyAlbum_122');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (122027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 122, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(12206, 122027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (122052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',122027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (122053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',122027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (122054, '1985-01-08', 'Perfecft!',122027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (122055, '1985-01-08', 'Beautiful colours. Nice close up. ',122027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (122029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 122, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(12206, 122029);
UPDATE Album set coveringImage_id=122027 where id = 1220;
---------------------------------------------------------------------
-- ALBUM - MyShelf - 123
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (123, 'MyAlbum 123', 'MyAlbum pictures',  121, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (12306,  'MyAlbum_123');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (123027, '117215467_5cccef9aaa_b.jpg', '117215467_5cccef9aaa_b.jpg', 'Water - 117215467_5cccef9aaa_b.jpg image',  '2009-12-18', 123, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(12306, 123027);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (123052, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',123027, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (123053, '1985-01-08', 'love every thing about this picture, really beautiful... :))',123027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (123054, '1985-01-08', 'Perfecft!',123027, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (123055, '1985-01-08', 'Beautiful colours. Nice close up. ',123027, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (123029, '205579493_baf0f850d1_o.jpg', '205579493_baf0f850d1_o.jpg', 'Water - 205579493_baf0f850d1_o.jpg image',  '2009-12-18', 123, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(12306, 123029);
UPDATE Album set coveringImage_id=123027 where id = 1230;

-------------------------------
-- Event Categories and Events
-------------------------------

INSERT INTO EventCategory(description) VALUES('Movie');
INSERT INTO EventCategory(description) VALUES('Concert');
INSERT INTO EventCategory(description) VALUES('Meeting');

INSERT INTO Event(id, name, description, CATEGORY_ID) VALUES (1, 'Rock concert of the decade', 'Get ready to rock your night away with this megaconcert extravaganza from 10 of the biggest rock stars of the 80''s', 1);
INSERT INTO Event(id, name, description, CATEGORY_ID) VALUES (2, 'Shane''s Sock Puppets', 'This critically acclaimed masterpiece will take you on an emotional rollercoaster the likes of which you''ve never experienced.', 2);

INSERT INTO Shelf(id, name, description, owner_id, event_id, created, shared) VALUES (10, 'Rock concert of the decade', 'event shelf', 1, 1, '2009-12-18', true);
INSERT INTO Shelf(id, name, description, owner_id, event_id, created, shared) VALUES (11, 'Shane''s Sock Puppets', 'event shelf', 1, 2, '2009-12-18', true);

UPDATE Event set shelf_id = 10 where id = 1;
UPDATE Event set shelf_id = 11 where id = 2;