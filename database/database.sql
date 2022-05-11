-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Item` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Item` (
  `UUID` BINARY(16) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `barcode` VARCHAR(13) NOT NULL,
  `tax` ENUM('A', 'B', 'C', 'D') NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `createdTime` TIMESTAMP NOT NULL,
  `modifiedTime` TIMESTAMP NOT NULL,
  PRIMARY KEY (`UUID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Company`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Company` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Company` (
  `UUID` BINARY(16) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `taxNumber` VARCHAR(45) NOT NULL,
  `registrationNumber` VARCHAR(45) NOT NULL,
  `taxpayer` TINYINT NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `post` VARCHAR(45) NOT NULL,
  `postNumb` VARCHAR(45) NOT NULL,
  `createdTime` TIMESTAMP NOT NULL,
  `modifiedTime` TIMESTAMP NOT NULL,
  PRIMARY KEY (`UUID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Invoice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Invoice` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Invoice` (
  `UUID` BINARY(16) NOT NULL,
  `priceToPay` DECIMAL(10,2) NOT NULL,
  `invoiceNumber` VARCHAR(45) NOT NULL,
  `discount` DECIMAL(10,2) NOT NULL,
  `card` VARCHAR(45) NOT NULL,
  `cashierId` VARCHAR(45) NOT NULL,
  `branch` VARCHAR(45) NOT NULL,
  `register` VARCHAR(45) NOT NULL,
  `transNumb` VARCHAR(45) NOT NULL,
  `createdTime` TIMESTAMP NOT NULL,
  `modifiedTime` TIMESTAMP NOT NULL,
  `Customer_UUID` BINARY(16) NOT NULL,
  `Issuer_UUID` BINARY(16) NOT NULL,
  PRIMARY KEY (`UUID`, `Customer_UUID`, `Issuer_UUID`),
  INDEX `fk_Invoice_Company_idx` (`Customer_UUID` ASC) VISIBLE,
  INDEX `fk_Invoice_Company1_idx` (`Issuer_UUID` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Items`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Items` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Items` (
  `Item_UUID` BINARY(16) NOT NULL,
  `Invoice_UUID` BINARY(16) NOT NULL,
  `UUID` BINARY(16) NOT NULL,
  `createdTime` DATETIME NOT NULL,
  `modifiedTime` DATETIME NOT NULL,
  `quantity` DECIMAL(10,2) NOT NULL,
  `discount` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`Item_UUID`, `Invoice_UUID`, `UUID`),
  INDEX `fk_Item_has_Invoice_Invoice1_idx` (`Invoice_UUID` ASC) VISIBLE,
  INDEX `fk_Item_has_Invoice_Item1_idx` (`Item_UUID` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Department`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Department` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Department` (
  `id` VARCHAR(3) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`InternalItem`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`InternalItem` ;

CREATE TABLE IF NOT EXISTS `mydb`.`InternalItem` (
  `UUID` BINARY(16) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `barcode` VARCHAR(13) NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `createdTime` TIMESTAMP NOT NULL,
  `modifiedTime` TIMESTAMP NOT NULL,
  `internalId` VARCHAR(4) NOT NULL,
  `tax` ENUM('A', 'B', 'C', 'D') NOT NULL,
  `Department_id` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`UUID`, `Department_id`),
  INDEX `fk_InternalItem_Departmen1_idx` (`Department_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`InternalItems`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`InternalItems` ;

CREATE TABLE IF NOT EXISTS `mydb`.`InternalItems` (
  `Invoice_UUID` BINARY(16) NOT NULL,
  `UUID` BINARY(16) NOT NULL,
  `createdTime` DATETIME NOT NULL,
  `modifiedTime` DATETIME NOT NULL,
  `InternalItem_UUID` BINARY(16) NOT NULL,
  `quantity` DECIMAL(10,2) NOT NULL,
  `discount` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`Invoice_UUID`, `UUID`, `InternalItem_UUID`),
  INDEX `fk_Item_has_Invoice_Invoice1_idx` (`Invoice_UUID` ASC) VISIBLE,
  INDEX `fk_Item_has_Invoice_copy1_InternalItem1_idx` (`InternalItem_UUID` ASC) VISIBLE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
