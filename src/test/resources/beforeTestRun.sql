/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;


DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`)
);

CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `details` varchar(255) DEFAULT NULL,
  `ean_code` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `price` decimal(19,2) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `quantity_unit` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
);

INSERT INTO `category` (`id`, `created_at`, `updated_at`, `version`, `description`, `name`, `parent_id`)
VALUES
  (1, '2016-09-04 22:07:26', '2016-09-04 22:07:26', 0, 'Contains household goods', 'Houshold', NULL),
  (2, '2016-09-04 22:17:28', '2016-09-04 23:56:03', 1, 'Contains kitchen supply stuff', 'Kitchen Supplies', 1),
  (5, '2016-09-05 13:57:57', '2016-09-05 13:57:57', 0, 'Contains Wardrobe stuff', 'Wardrobes', 1),
  (6, '2016-09-05 14:22:36', '2016-09-05 14:22:36', 0, 'Livingroom supplies', 'Livingroom Supplies', 1);


INSERT INTO `product` (`id`, `created_at`, `updated_at`, `version`, `details`, `ean_code`, `is_active`, `price`, `quantity`, `quantity_unit`, `title`, `category_id`)
VALUES
  (1, '2016-09-05 01:31:19', '2016-09-05 01:31:19', 0, 'Utensil Set from ABC', '1qazxswed', 1, 20.00, 1, 'set', 'Abc Utensil Set', 2),
  (2, '2016-09-05 10:55:00', '2016-09-05 11:37:32', 2, 'XYZ Teapot Set from XYZ', 'qwertyuio', 1, 15.00, 1, 'set', 'XYZ Teapot Set', 1),
  (3, '2016-09-05 13:21:12', '2016-09-05 13:21:12', 0, 'PQR glassware set', 'asdfghjk', 1, 12.00, 1, 'set', 'PQR glassware set', 2),
  (4, '2016-09-05 13:56:43', '2016-09-05 13:56:43', 0, 'NOP bottles set', 'zasdfg', 1, 10.00, 1, 'set', 'NOP bottles set', 2);




/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
