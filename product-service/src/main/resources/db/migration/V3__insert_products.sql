-- Insert products for the 'Keyboards' category
INSERT INTO products (available_quantity, description, name, price, category_id)
VALUES
     (10, 'Mechanical keyboard with RGB lighting', 'Mechanical Keyboard 1', 99.99, 1),
     (15, 'Wireless compact keyboard', 'Wireless Compact Keyboard 1', 79.99, 1),
     (20, 'Backlit gaming keyboard with customizable keys', 'Gaming Keyboard 1', 129.99, 1),
     (25, 'Mechanical keyboard with wrist rest', 'Ergonomic Keyboard 1', 109.99, 1),
     (18, 'Wireless keyboard and mouse combo', 'Wireless Combo 1', 69.99, 1);

-- Insert products for the 'Monitors' category
INSERT INTO products (available_quantity, description, name, price, category_id)
VALUES
     (30, '27-inch IPS monitor with 4K resolution', '4K Monitor 1', 399.99, 2),
     (25, 'Ultra-wide gaming monitor with HDR support', 'Ultra-wide Gaming Monitor 1', 499.99, 2),
     (22, '24-inch LED monitor for office use', 'Office Monitor 1', 179.99, 2),
     (28, '32-inch curved monitor with AMD FreeSync', 'Curved Monitor 1', 329.99, 2),
     (35, 'Portable USB-C monitor for laptops', 'Portable Monitor 1', 249.99, 2);

-- Insert products for the 'Screens' category
INSERT INTO products (available_quantity, description, name, price, category_id)
VALUES
     (15, 'Curved OLED gaming screen with 240Hz refresh rate', 'Curved OLED Gaming Screen 1', 799.99, 3),
     (18, 'Flat QLED monitor with 1440p resolution', 'QLED Monitor 1', 599.99, 3),
     (22, '27-inch touch screen display for creative work', 'Touch Screen Display 1', 699.99, 3),
     (20, 'Ultra-slim 4K HDR display for multimedia', 'Ultra-slim 4K HDR Display 1', 449.99, 3),
     (25, 'Gaming projector with low input lag', 'Gaming Projector 1', 899.99, 3);

-- Insert products for the 'Mice' category
INSERT INTO products (available_quantity, description, name, price, category_id)
VALUES
     (30, 'Wireless gaming mouse with customizable RGB lighting', 'RGB Gaming Mouse 1', 59.99, 4),
     (28, 'Ergonomic wired mouse for productivity', 'Ergonomic Wired Mouse 1', 29.99, 4),
     (32, 'Ambidextrous gaming mouse with high DPI', 'Ambidextrous Gaming Mouse 1', 69.99, 4),
     (26, 'Travel-sized compact mouse for laptops', 'Travel Mouse 1', 19.99, 4),
     (35, 'Vertical ergonomic mouse for reduced strain', 'Vertical Ergonomic Mouse 1', 39.99, 4);

-- Insert products for the 'Accessories' category
INSERT INTO products (available_quantity, description, name, price, category_id)
VALUES
     (25, 'Adjustable laptop stand with cooling fan', 'Adjustable Laptop Stand 1', 34.99, 5),
     (20, 'Wireless charging pad for smartphones', 'Wireless Charging Pad 1', 24.99, 5),
     (28, 'Gaming headset stand with RGB lighting', 'RGB Headset Stand 1', 49.99, 5),
     (22, 'Bluetooth mechanical keypad for tablets', 'Bluetooth Keypad 1', 39.99, 5),
     (30, 'External hard drive enclosure with USB-C', 'External Hard Drive Enclosure 1', 29.99, 5);
