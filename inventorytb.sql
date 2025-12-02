-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 09, 2023 at 06:47 PM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.1.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `inventorytb`
--

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `ID_Barang` int(10) NOT NULL,
  `ID_Distributor` int(15) NOT NULL,
  `Nama_Barang` varchar(50) NOT NULL,
  `Satuan` varchar(10) NOT NULL,
  `Harga` int(25) NOT NULL,
  `Stok` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`ID_Barang`, `ID_Distributor`, `Nama_Barang`, `Satuan`, `Harga`, `Stok`) VALUES
(111, 666, 'Keramik', 'Dus', 1200, 4),
(222, 12345, 'Cat', 'Kaleng', 50000, 20),
(5555, 12345, 'wdwdw', 'dwdw', 77, 12122);

-- --------------------------------------------------------

--
-- Table structure for table `distributor`
--

CREATE TABLE `distributor` (
  `ID_Distributor` int(15) NOT NULL,
  `Nama_Distributor` varchar(50) NOT NULL,
  `Alamat` varchar(100) NOT NULL,
  `No_Telepon` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `distributor`
--

INSERT INTO `distributor` (`ID_Distributor`, `Nama_Distributor`, `Alamat`, `No_Telepon`) VALUES
(666, 'qwqw', 'mkmm', 123456),
(888, 'Ahmad', 'kokokkkaka', 12345678),
(12345, 'Budi', 'Magelang', 125435345);

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `ID_User` int(10) NOT NULL,
  `Username` varchar(10) NOT NULL,
  `Password` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`ID_User`, `Username`, `Password`) VALUES
(1234, 'Admin', '12345');

-- --------------------------------------------------------

--
-- Table structure for table `pesan`
--

CREATE TABLE `pesan` (
  `ID_Order` int(10) NOT NULL,
  `Nama_Barang` varchar(50) NOT NULL,
  `Jumlah` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pesan`
--

INSERT INTO `pesan` (`ID_Order`, `Nama_Barang`, `Jumlah`) VALUES
(1, 'Cat', 12),
(33, 'Cat', 10),
(123, 'Cat', 23);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`ID_Barang`),
  ADD KEY `ID_Distributor` (`ID_Distributor`);

--
-- Indexes for table `distributor`
--
ALTER TABLE `distributor`
  ADD PRIMARY KEY (`ID_Distributor`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`ID_User`);

--
-- Indexes for table `pesan`
--
ALTER TABLE `pesan`
  ADD PRIMARY KEY (`ID_Order`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `barang`
--
ALTER TABLE `barang`
  MODIFY `ID_Barang` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5556;

--
-- AUTO_INCREMENT for table `distributor`
--
ALTER TABLE `distributor`
  MODIFY `ID_Distributor` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12346;

--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `ID_User` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1235;

--
-- AUTO_INCREMENT for table `pesan`
--
ALTER TABLE `pesan`
  MODIFY `ID_Order` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3124;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `barang`
--
ALTER TABLE `barang`
  ADD CONSTRAINT `ID_Distributor` FOREIGN KEY (`ID_Distributor`) REFERENCES `distributor` (`ID_Distributor`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
