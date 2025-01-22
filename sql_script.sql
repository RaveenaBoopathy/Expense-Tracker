SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema expenses_tracker
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `expenses_tracker` DEFAULT CHARACTER SET utf8mb4;
USE `expenses_tracker`;

-- -----------------------------------------------------
-- Table `expenses_tracker`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expenses_tracker`.`category` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,  -- Added AUTO_INCREMENT
  `name` VARCHAR(255) NOT NULL,  -- Changed to NOT NULL
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `expenses_tracker`.`client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expenses_tracker`.`client` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `first_name` VARCHAR(255) NULL DEFAULT NULL,
  `last_name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 9 DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `expenses_tracker`.`expense`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expenses_tracker`.`expense` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `amount` DECIMAL(10,2) NOT NULL,
  `date_time` DATETIME NULL DEFAULT NULL,
  `description` VARCHAR(400) NULL DEFAULT NULL,
  `category_id` INT(11) NULL,
  `client_id` INT(11) NOT NULL,  -- Changed to NOT NULL
  PRIMARY KEY (`id`),
  INDEX `FKmvjm59reb5i075vu38bwcaqj6` (`category_id` ASC),
  INDEX `FKq0le09a1upxs1ctbr5mpoltep` (`client_id` ASC),
  CONSTRAINT `FKmvjm59reb5i075vu38bwcaqj6`
    FOREIGN KEY (`category_id`)
    REFERENCES `expenses_tracker`.`category` (`id`)
    ON DELETE SET NULL,  -- Set category to NULL if category is deleted
  CONSTRAINT `FKq0le09a1upxs1ctbr5mpoltep`
    FOREIGN KEY (`client_id`)
    REFERENCES `expenses_tracker`.`client` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 16 DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `expenses_tracker`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expenses_tracker`.`role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `expenses_tracker`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expenses_tracker`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(255) NULL DEFAULT NULL,
  `user_name` VARCHAR(255) NULL DEFAULT NULL,
  `client_id` INT(11) NOT NULL,  -- Changed to NOT NULL
  `enabled` BIT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_rb7eox526ilbewv2wuv5bnsrt` (`client_id`),
  UNIQUE INDEX `UK_user_name` (`user_name`),  -- Ensure username is unique
  CONSTRAINT `FKrl8au09hfjd9742b89li2rb3`
    FOREIGN KEY (`client_id`)
    REFERENCES `expenses_tracker`.`client` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 9 DEFAULT CHARACTER SET = utf8mb4;

-- -----------------------------------------------------
-- Table `expenses_tracker`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `expenses_tracker`.`users_roles` (
  `user_id` INT(11) NOT NULL,
  `role_id` INT(11) NOT NULL,
  INDEX `FKt4v0rrweyk393bdgt107vdx0x` (`role_id` ASC),
  INDEX `FKgd3iendaoyh04b95ykqise6qh` (`user_id` ASC),
  CONSTRAINT `FKgd3iendaoyh04b95ykqise6qh`
    FOREIGN KEY (`user_id`)
    REFERENCES `expenses_tracker`.`user` (`id`)
    ON DELETE CASCADE,  -- Delete user roles if user is deleted
  CONSTRAINT `FKt4v0rrweyk393bdgt107vdx0x`
    FOREIGN KEY (`role_id`)
    REFERENCES `expenses_tracker`.`role` (`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
