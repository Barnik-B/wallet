CREATE TABLE user (
   id INT NOT NULL AUTO_INCREMENT,
   balance DOUBLE NOT NULL DEFAULT 0,
   PRIMARY KEY (id)
);

CREATE TABLE transactions (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE transfers (
    id INT NOT NULL AUTO_INCREMENT,
    transaction_id INT NOT NULL,
    receiver_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (receiver_id) REFERENCES User(id),
    FOREIGN KEY (transaction_id) REFERENCES Transactions(id)
);