INSERT INTO User(id, firstname, secondname, email, login, passwordHash, birthdate, sex, hasAvatar, preDefined) VALUES (1,  'John',   'Smith',   'jsmith@jboss.com',     'Noname',   '8cb2237d0679ca88db6464eac60da96345513964',  '1970-01-08', 1, false, true);

INSERT INTO Shelf(id, name, description, owner_id, created, shared) VALUES (1, 'Nature',       'Nature pictures',        1, '2009-12-18', true);

---------------------------------------------------------------------
-- ALBUM - Animals"
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (0, 'Animals', 'Animals pictures',  1, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (0,  'Animals');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (0, '1750979205_6e51b47ce9_o.jpg', '1750979205_6e51b47ce9_o.jpg', 'Animals - 1750979205_6e51b47ce9_o.jpg image',  '2009-12-18', 0, 'Canon EOS 450D', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 0);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (0, '1985-01-08', 'this is extremely Good:) Congratulations!',0, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (1, '1906662004_655d0f6ccf_o.jpg', '1906662004_655d0f6ccf_o.jpg', 'Animals - 1906662004_655d0f6ccf_o.jpg image',  '2009-12-18', 0, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (1, '1985-01-08', 'nice shot =) ',1, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (2, '1985-01-08', 'nice shot =) ',1, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (2, '2090459727_f2888e5cbe_o.jpg', '2090459727_f2888e5cbe_o.jpg', 'Animals - 2090459727_f2888e5cbe_o.jpg image',  '2009-12-18', 0, 'Canon PowerShot SX110 IS', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (3, '2297752925_de29b5fb10_o.jpg', '2297752925_de29b5fb10_o.jpg', 'Animals - 2297752925_de29b5fb10_o.jpg image',  '2009-12-18', 0, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (3, '1985-01-08', 'that is a beautiful flower with great colours ',3, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (4, '1985-01-08', 'Fantastic job. Great light and color! Great shot!',3, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (5, '1985-01-08', '|Wonderful| coloured flower .... *excellent* macro .... -nice- details!!!',3, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (6, '1985-01-08', 'Gorgeous! Lovely color!',3, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (4, '2298556444_2151b7a6c4_o.jpg', '2298556444_2151b7a6c4_o.jpg', 'Animals - 2298556444_2151b7a6c4_o.jpg image',  '2009-12-18', 0, 'Sony Alpha DSLR-A350', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 4);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (7, '1985-01-08', 'Stunning capture! :-)',4, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (8, '1985-01-08', 'Wonderful.',4, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (5, '2508246015_313952406c_o.jpg', '2508246015_313952406c_o.jpg', 'Animals - 2508246015_313952406c_o.jpg image',  '2009-12-18', 0, 'Canon Digital IXUS 80 IS (PowerShot SD1100 IS)', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 5);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (9, '1985-01-08', 'Gorgeous! Lovely color!',5, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (6, '2521898117_f2ebf233c9_o.jpg', '2521898117_f2ebf233c9_o.jpg', 'Animals - 2521898117_f2ebf233c9_o.jpg image',  '2009-12-18', 0, 'Pentax Optio E40', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 6);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (10, '1985-01-08', 'this is extremely Good:) Congratulations!',6, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (11, '1985-01-08', 'fantastic shot !!!!!!',6, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (7, '308709862_f8f7fbcec4_b.jpg', '308709862_f8f7fbcec4_b.jpg', 'Animals - 308709862_f8f7fbcec4_b.jpg image',  '2009-12-18', 0, 'Olympus Stylus mju 1040', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 7);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (12, '1985-01-08', 'Superb Shot and so beautiful Colors !!! ',7, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (13, '1985-01-08', 'really pretty. it looks like there is a lady in the _center_, blowing kisses!!',7, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (8, '3390059723_4b2883ee59_o.jpg', '3390059723_4b2883ee59_o.jpg', 'Animals - 3390059723_4b2883ee59_o.jpg image',  '2009-12-18', 0, 'BBK DP810', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 8);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (14, '1985-01-08', 'Wonderful.',8, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (15, '1985-01-08', 'Amazing shot..',8, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (16, '1985-01-08', 'Wow!! Macro stupenda!!! Complimenti! ',8, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (17, '1985-01-08', 'Fantastic job. Great light and color! Great shot!',8, 1);
