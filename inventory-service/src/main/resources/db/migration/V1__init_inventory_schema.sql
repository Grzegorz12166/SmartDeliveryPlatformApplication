CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(19,2) NOT NULL CHECK (price >= 0),
    currency VARCHAR(3) NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_item (
    product_id UUID PRIMARY KEY REFERENCES product(id) ON DELETE CASCADE,
    available_quantity INTEGER NOT NULL CHECK (available_quantity >= 0)
);

CREATE TABLE IF NOT EXISTS stock_reservation (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS stock_reservation_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reservation_id UUID NOT NULL REFERENCES stock_reservation(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES product(id),
    quantity INTEGER NOT NULL CHECK (quantity > 0)
);

CREATE TABLE IF NOT EXISTS processed_event (
    id UUID PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL DEFAULT now()
);
