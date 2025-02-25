CREATE TABLE Models (
    ModelID SERIAL PRIMARY KEY,
    ModelName VARCHAR(100) NOT NULL,
    Seater INT NOT NULL CHECK (Seater > 0),
    FuelType VARCHAR(50) NOT NULL CHECK (FuelType IN ('Petrol', 'Diesel', 'Electric', 'Hybrid')),
    Price DECIMAL(10,2) NOT NULL CHECK (Price > 0)
);

CREATE TABLE Cars (
    CarID SERIAL PRIMARY KEY,
    Company VARCHAR(100) NOT NULL,
    ModelID INT NOT NULL,
    Sold BOOLEAN DEFAULT FALSE,
    Type VARCHAR(50) NOT NULL CHECK (Type IN ('SUV', 'Sedan', 'Maruti', 'Suzuki', 'Honda')),
    FOREIGN KEY (ModelID) REFERENCES Models(ModelID) ON DELETE CASCADE
);
