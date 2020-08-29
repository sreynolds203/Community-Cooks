CREATE TABLE recipes(
	recipe_id BIGSERIAL,
	recipe_name VARCHAR(100),
	author VARCHAR(50),
	CONSTRAINT recipe_id PRIMARY KEY (recipe_id)
);

CREATE TABLE ingredients(
	ingredient_id BIGSERIAL,
	recipe_id BIGINT REFERENCES recipes (recipe_id) ON DELETE CASCADE,
	ingredient VARCHAR(100),
	quantity INTEGER,
	measaurement VARCHAR(50)
	CONSTRAINT ingredient_id PRIMARY KEY (ingredient_id)
);

CREATE TABLE steps(
	steps_id BIGSERIAL,
	recipe_id BIGINT REFERENCES recipes (recipe_id) ON DELETE CASCADE,
	steps VARCHAR,
	CONSTRAINT steps_id PRIMARY KEY (steps_id)
);

CREATE TABLE users(
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	email VARCHAR(100),
	username VARCHAR(50),
	user_pass VARCHAR(100),
	CONSTRAINT username PRIMARY KEY (username)
);