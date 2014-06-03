----
-- This will populate the database with a small amount of entites for testing purposes

INSERT INTO User(id, fbId, gplusId, firstname, secondname, email, login, passwordHash, birthdate, sex, hasAvatar, preDefined) VALUES (1,  '1', '1', 'John',   'Smith',   'jsmith@jboss.com',     'Noname',   '8cb2237d0679ca88db6464eac60da96345513964',  '1970-01-08', 1, false, true);
INSERT INTO User(id, fbId, gplusId, firstname, secondname, email, login, passwordHash, birthdate, sex, hasAvatar, preDefined) VALUES (2,  '1', '1', 'Jack',   'Smith',   'jasmith@jboss.com',     'Noname2',   '8cb2237d0679ca88db6464eac60da96345513964',  '1970-03-08', 1, false, false);

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
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (2, '1985-01-08', 'nice shot =) ',1, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (2, '2090459727_f2888e5cbe_o.jpg', '2090459727_f2888e5cbe_o.jpg', 'Animals - 2090459727_f2888e5cbe_o.jpg image',  '2009-12-18', 0, 'Canon PowerShot SX110 IS', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (3, '2297752925_de29b5fb10_o.jpg', '2297752925_de29b5fb10_o.jpg', 'Animals - 2297752925_de29b5fb10_o.jpg image',  '2009-12-18', 0, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(0, 3);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (3, '1985-01-08', 'that is a beautiful flower with great colours ',3, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (4, '1985-01-08', 'Fantastic job. Great light and color! Great shot!',3, 2);

---------------------------------------------------------------------
-- ALBUM - Nature"
---------------------------------------------------------------------
INSERT INTO Album(id, name, description, shelf_id, created) VALUES (1, 'Nature', 'Nature pictures',  1, '2009-12-18');
INSERT INTO MetaTag(id, tag) VALUES (1,  'Nature');

INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (4, '01[303x202].jpg', '01[303x202].jpg', 'Nature - 01[303x202].jpg image',  '2009-12-18', 1, 'LG LDC-A310', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 4);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (5, '1985-01-08', 'Perfecft!',4, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (6, '1985-01-08', 'I Think this is Art!',4, 2);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (7, '1985-01-08', 'that is a beautiful flower with great colours ',4, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (5, '1[305x457].jpg', '1[305x457].jpg', 'Nature - 1[305x457].jpg image',  '2009-12-18', 1, 'Canon EOS 450D', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 5);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (8, '1985-01-08', 'really pretty. it looks like there is a lady in the _center_, blowing kisses!!',5, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (6, '273927725_c9f5ef5952_o.jpg', '273927725_c9f5ef5952_o.jpg', 'Nature - 273927725_c9f5ef5952_o.jpg image',  '2009-12-18', 1, 'Sony CyberShot DSC-T77', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 6);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (9, '1985-01-08', 'Very *lovely*',6, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (10, '1985-01-08', 'Very *lovely*',6, 2);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (7, '2[303x457].jpg', '2[303x457].jpg', 'Nature - 2[303x457].jpg image',  '2009-12-18', 1, 'Canon PowerShot SX110 IS', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 7);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (11, '1985-01-08', 'Beautiful ^Flower^...great Macro....Excellent !!!',7, 1);
  INSERT INTO Comment(id, date, message, image_id, author_id) VALUES (12, '1985-01-08', 'I Think this is Art!',7, 1);
INSERT INTO Image(id, name, path, description, created, album_id, cameraModel, width, size, height, uploaded, allowComments, showMetaInfo) VALUES (8, '3392730627_1cdb18cba6_o.jpg', '3392730627_1cdb18cba6_o.jpg', 'Nature - 3392730627_1cdb18cba6_o.jpg image',  '2009-12-18', 1, 'Nikon D60', 1024, 1917, 768, '2009-12-01', true, true);
  INSERT INTO Image_MetaTag(IMAGETAGS_ID, IMAGES_ID) VALUES(1, 8);