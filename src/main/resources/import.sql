/*------------------------------------- EJEMPLOS CLIENTES -------------------------------------*/
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('john', 'doe', 'john@gmail.com', 'Calle Leonardo da Vinci, 7, 41092', '9 5555 2222', NOW());
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('jane', 'doe', 'jane@gmail.com', 'Calle de Velázquez, 80 Madrid', '9 8888 2222', NOW());
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('richard', 'lee', 'rlee@gmail.com', 'Calle 12 entre 51 y 53', '9 0222 2635', NOW());
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('Caleb', 'Shields', 'pede.praesent@icloud.ca', 'Calle Leonardo da Vinci, 7, 41092', '9 8729 6232', NOW());
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('Sonya', 'Valentine', 'eget.mollis@protonmail.edu', '1843 Ac Ave', '9 7213 2805', NOW());
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('Kasimir', 'Donovan', 'libero.nec@hotmail.org', 'Ap #473-4963 Quis, St.', '9 2112 7371', NOW());
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('Hanae', 'Shepard', 'morbi.neque.tellus@aol.net', 'Calle Leonardo da Vinci, 7, 41092', '9 9815 7972', NOW());
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('Amal', 'Merrill', 'cursus.et@outlook.org', 'Ap #948-7662 Eu Road', '9 5494 6762', NOW());
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('Callum', 'Ferguson', 'nibh@google.net', '377-6338 Orci Rd.', '9 3746 6831', NOW());
INSERT INTO clients (name, lastname, email, address, phone, create_at) VALUES('Isaac', 'Leonard', 'auctor@aol.couk', '5211 Tempus, Street', '9 1180 8266', NOW());

/*------------------------------------- EJEMPLOS ROLES -------------------------------------*/
INSERT INTO roles (role_name) VALUES('ROLE_ADMIN');
INSERT INTO roles (role_name) VALUES('ROLE_USER');

/*------------------------------------- EJEMPLOS USUARIOS -------------------------------------*/
INSERT INTO users (name, lastname, username, email, password, create_at) VALUES('Julio', 'Cortazar', 'administrador', 'admin@email.com', '$2a$10$4B5RdMmVQijOGQwYEQNdhOwf340PUdTMZGG6ycUoiy4dLtVnI5ASm', NOW());
INSERT INTO users (name, lastname, username, email, password, create_at) VALUES('Pablo', 'Neruda', 'usuario', 'user@email.com', '$2a$10$gxmv4.jy0x0KN4BLXGwwGOeTF1tumJh/zj7ZZEDZY1eL5FsOExIUG', NOW());

/*------------------------------------- EJEMPLOS ROLES_USUARIOS -------------------------------------*/
INSERT INTO user_role (user_id, role_id) VALUES(1, 1);
INSERT INTO user_role (user_id, role_id) VALUES(1, 2);
INSERT INTO user_role (user_id, role_id) VALUES(2, 2);

/*------------------------------------- EJEMPLOS AUTORES -------------------------------------*/
INSERT INTO authors (name, lastname, country, create_at) VALUES('Edgar Allan', 'Poe', 'EE.UU.', NOW());
INSERT INTO authors (name, lastname, country, create_at) VALUES('Gabriel', 'Garcia Marquez', 'Colombia', NOW());
INSERT INTO authors (name, lastname, country, create_at) VALUES('John Ronald', 'Reuel Tolkien', 'Inglaterra', NOW());
INSERT INTO authors (name, lastname, country, create_at) VALUES('Stephen', 'King', 'EE.UU.', NOW());
INSERT INTO authors (name, lastname, country, create_at) VALUES('Arthur Conan', 'Doyle', 'Inglaterra', NOW());

/*------------------------------------- EJEMPLOS RELACION EDITORIAL -------------------------------------*/
INSERT INTO editorials (name, country, create_at) VALUES('Alma Europa', 'España', NOW());
INSERT INTO editorials (name, country, create_at) VALUES('Createspace Independient Publishing Platform', 'EE.UU.', NOW());
INSERT INTO editorials (name, country, create_at) VALUES('DeBolsillo', 'España', NOW());
INSERT INTO editorials (name, country, create_at) VALUES('Minotauro', 'España', NOW());
INSERT INTO editorials (name, country, create_at) VALUES('Penguin Random House', 'EE.UU.', NOW());
INSERT INTO editorials (name, country, create_at) VALUES('Mestas Ediciones, S.L.', 'España', NOW());

/*------------------------------------- EJEMPLOS LIBROS -------------------------------------*/
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('El Cuervo', 'El cuervo es un poema narrativo escrito por Edgar Allan Poe, publicado por primera vez en 1845. Constituye su composición poética más famosa, ya que le dio reconocimiento internacional. Son notables su musicalidad, el lenguaje estilizado y la atmósfera sobrenatural que logra recrear', 'DISPONIBLE', '', 5, 22000.0, 1, 1, NOW());
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('El Corazon Delator', 'El corazón delator —en inglés original The Tell-Tale Heart— es un cuento del escritor estadounidense Edgar Allan Poe clasificado en la narrativa gótica, publicado por primera vez en el periódico literario The Pioneer en enero de 1843. ', 'POCAS_UNIDADES', 'Solo en idioma ingles', 2, 21000.0, 1, 2, NOW());
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('Cien Años de Soledad', 'Cien años de soledad es una novela del escritor colombiano Gabriel García Márquez, ganador del Premio Nobel de Literatura en 1982. Es considerada una obra maestra de la literatura hispanoamericana y universal, así como una de las obras más traducidas y leídas en español', 'POCAS_UNIDADES', '', 1, 32000.0, 2, 3, NOW());
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('Cronica de una Muerte Anunciada', 'Crónica de una muerte anunciada es una novela del escritor colombiano Gabriel García Márquez, publicada por primera vez en 1981. Fue incluida en la lista de las 100 mejores novelas en español del siglo XX del periódico español El Mundo', 'POCAS_UNIDADES', '', 2, 42000.0, 2, 3, NOW());
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('La Caida de Gondolin', 'a caída de Gondolin» es un relato del escritor británico J. R. R. Tolkien, publicado de forma póstuma por su tercer hijo y principal editor, Christopher Tolkien, en el segundo volumen de La historia de la Tierra Media: El libro de los cuentos perdidos 2.', 'POCAS_UNIDADES', '', 2, 25000.0, 3, 4, NOW());
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('El Silmarillion', 'El Silmarillion es una recopilacion de obras de J.R.R. Tolkien, editada y publicada postumamente por su hijo Christopher Tolkien, en 1977. En ella se narra, entre otras cosas, la creación de Eä y el nacimiento de las razas más importantes de Arda.', 'DISPONIBLE', '', 14, 45000.0, 3, 4, NOW());
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('El Resplandor', 'El resplandor es la tercera novela de terror del escritor estadounidense Stephen King, publicada en 1977. El título se inspiró en la canción de John Lennon "Instant Karma!", que contiene la línea We all shine on...', 'SIN_EXISTENCIAS', 'Unidades solo en idioma ingles', 0, 37000.0, 4, 5, NOW());
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('Carrie', 'Carrie es la primera novela publicada por el escritor estadounidense Stephen King, en 1974.​ Es uno de los libros más censurados en las escuelas de EE. UU. y la película incluso estuvo prohibida en Finlandia.', 'POCAS_UNIDADES', '', 3, 34000.0, 4, 3, NOW());
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('Estudio en Escarlata', 'Un Estudio en escarlata es una novela de misterio escrita por Arthur Conan Doyle y publicada en la revista Beetons Christmas Annual de noviembre de 1887, con ilustraciones de David Henry Friston. Se trata de la primera aparición de Sherlock Holmes y el Dr. Watson.', 'DISPONIBLE', '', 14, 21000.0, 5, 6, NOW());
INSERT INTO books (name, description, status, observation, stock, price, author_id, editorial_id, create_at) VALUES('La Liga de los Pelirrojos', 'Con cargo al legado del difunto Bzekish Hopkins, de Lebanon, Pennsylvania, E.E. U.S., se ha producido otra vacante que de derecho a un miembro de la liga a percibir un salario de cuatro libros a la semana por servicios puramente nominales.', 'POCAS_UNIDADES', '', 3, 20000.0, 5, 2, NOW());

/*------------------------------------- EJEMPLOS GENERO -------------------------------------*/
INSERT INTO genres (name, create_at) VALUES('Cuento', NOW());
INSERT INTO genres (name, create_at) VALUES('Misterio', NOW());
INSERT INTO genres (name, create_at) VALUES('Gotico', NOW());
INSERT INTO genres (name, create_at) VALUES('Poema', NOW());
INSERT INTO genres (name, create_at) VALUES('Drama', NOW());
INSERT INTO genres (name, create_at) VALUES('Terror', NOW());
INSERT INTO genres (name, create_at) VALUES('Narracion', NOW());
INSERT INTO genres (name, create_at) VALUES('Novela', NOW());
INSERT INTO genres (name, create_at) VALUES('Fantasia', NOW());
INSERT INTO genres (name, create_at) VALUES('Relato', NOW());
INSERT INTO genres (name, create_at) VALUES('Suspenso', NOW());
INSERT INTO genres (name, create_at) VALUES('Ficcion', NOW());
INSERT INTO genres (name, create_at) VALUES('Realismo magico', NOW());

/*------------------------------------- EJEMPLOS RELACION LIBROS_GENEROS -------------------------------------*/
INSERT INTO book_genre (genre_id, book_id) VALUES(4, 1);
INSERT INTO book_genre (genre_id, book_id) VALUES(5, 1);
INSERT INTO book_genre (genre_id, book_id) VALUES(6, 1);
INSERT INTO book_genre (genre_id, book_id) VALUES(1, 2);
INSERT INTO book_genre (genre_id, book_id) VALUES(2, 2);
INSERT INTO book_genre (genre_id, book_id) VALUES(3, 2);
INSERT INTO book_genre (genre_id, book_id) VALUES(7, 3);
INSERT INTO book_genre (genre_id, book_id) VALUES(8, 3);
INSERT INTO book_genre (genre_id, book_id) VALUES(2, 4);
INSERT INTO book_genre (genre_id, book_id) VALUES(7, 4);
INSERT INTO book_genre (genre_id, book_id) VALUES(13, 4);
INSERT INTO book_genre (genre_id, book_id) VALUES(9, 5);
INSERT INTO book_genre (genre_id, book_id) VALUES(10, 5);
INSERT INTO book_genre (genre_id, book_id) VALUES(9, 6);
INSERT INTO book_genre (genre_id, book_id) VALUES(10, 6);
INSERT INTO book_genre (genre_id, book_id) VALUES(3, 7);
INSERT INTO book_genre (genre_id, book_id) VALUES(5, 7);
INSERT INTO book_genre (genre_id, book_id) VALUES(6, 7);
INSERT INTO book_genre (genre_id, book_id) VALUES(11, 7);
INSERT INTO book_genre (genre_id, book_id) VALUES(12, 7);
INSERT INTO book_genre (genre_id, book_id) VALUES(6, 8);
INSERT INTO book_genre (genre_id, book_id) VALUES(8, 8);
INSERT INTO book_genre (genre_id, book_id) VALUES(11, 8);
INSERT INTO book_genre (genre_id, book_id) VALUES(2, 9);
INSERT INTO book_genre (genre_id, book_id) VALUES(8, 9);
INSERT INTO book_genre (genre_id, book_id) VALUES(2, 10);
INSERT INTO book_genre (genre_id, book_id) VALUES(12, 10);