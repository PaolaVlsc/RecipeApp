-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 22, 2022 at 09:43 AM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `recipes_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `ID` int(4) NOT NULL,
  `name` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`ID`, `name`) VALUES
(1, 'Starters'),
(2, 'Main Courses'),
(3, 'Desserts');

-- --------------------------------------------------------

--
-- Table structure for table `recipe`
--

CREATE TABLE `recipe` (
  `ID` int(4) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `category` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `recipe`
--

INSERT INTO `recipe` (`ID`, `name`, `description`, `category`) VALUES
(1, 'Chicken soup', 'A comfort food whenever you feel sick. Refreshing soup and a classic meal', 1),
(2, 'Smashed potato', 'Do you love potatoes? Try this one with feta and gravy sauce', 2);

-- --------------------------------------------------------

--
-- Table structure for table `recipe_ingredients`
--

CREATE TABLE `recipe_ingredients` (
  `ID` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `measurement` varchar(30) DEFAULT NULL,
  `recipe` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `recipe_ingredients`
--

INSERT INTO `recipe_ingredients` (`ID`, `name`, `quantity`, `measurement`, `recipe`) VALUES
(1, 'avocado oil', 1, 'tablespoon', 1),
(2, 'garlic', 6, 'cloves', 1),
(3, 'onion', 1, 'ea', 1),
(4, 'carrots', 2, 'ea', 1),
(5, 'chicken broth', 6, 'cups', 1),
(6, 'boneless skinless chicken breast or thighs', 1, 'kg', 1),
(7, 'salt', 1, 'tsp', 1),
(8, 'pepper', 1, 'tsp', 1),
(9, 'potatoes', 6, 'kg', 2),
(10, 'salt', 1, 'teaspoons ', 2),
(11, 'garlic powder', 1.5, 'teaspoons ', 2),
(12, 'onion powder', 0.75, 'teaspoons ', 2),
(13, 'ground black pepper', 1, 'teaspoons ', 2),
(14, 'finely chopped fresh herbs', 1, 'tablespoons ', 2);

-- --------------------------------------------------------

--
-- Table structure for table `recipe_steps`
--

CREATE TABLE `recipe_steps` (
  `ID` bigint(20) NOT NULL,
  `description` text DEFAULT NULL,
  `recipe` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `recipe_steps`
--

INSERT INTO `recipe_steps` (`ID`, `description`, `recipe`) VALUES
(1, 'Put the chicken, carrots, celery and onion in a large soup pot and cover with cold water. Heat and simmer, uncovered, until the chicken meat falls off of the bones (skim off foam every so often).', 1),
(2, 'Take everything out of the pot. Strain the broth. Pick the meat off of the bones and chop the carrots, celery and onion. Season the broth with salt, pepper and chicken bouillon to taste, if desired. Return the chicken, carrots, celery and onion to the pot, stir together, and serve.', 1),
(3, 'Preheat the oven to 425°F and liberally coat 2 baking sheets with olive oil.', 2),
(4, 'Place the potatoes and 1 teaspoon of the salt in a large pot and fill it with enough water to cover the potatoes by 1 inch. Bring to a boil and cook until the potatoes are soft and fork-tender, 15 to 20 minutes.', 2),
(5, 'Drain the potatoes and let them cool slightly. Place each potato onto the oiled baking sheet and use the back of a measuring cup to smash them down until they’re about ¼-inch thick. Drizzle with the olive oil, and sprinkle with the garlic powder, onion powder, remaining 1 teaspoon salt, and pepper. Roast 25 to 35 minutes, or until golden brown and crisp around the edges, rotating the pans halfway.', 2),
(6, 'Season to taste with more sea salt, or flaky sea salt, fresh herbs, and sprinkles of Parmesan, if desired.', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `recipe`
--
ALTER TABLE `recipe`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `category` (`category`);

--
-- Indexes for table `recipe_ingredients`
--
ALTER TABLE `recipe_ingredients`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `recipe` (`recipe`);

--
-- Indexes for table `recipe_steps`
--
ALTER TABLE `recipe_steps`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `recipe` (`recipe`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `ID` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `recipe`
--
ALTER TABLE `recipe`
  MODIFY `ID` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `recipe_ingredients`
--
ALTER TABLE `recipe_ingredients`
  MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `recipe_steps`
--
ALTER TABLE `recipe_steps`
  MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `recipe`
--
ALTER TABLE `recipe`
  ADD CONSTRAINT `recipe_ibfk_1` FOREIGN KEY (`category`) REFERENCES `category` (`ID`);

--
-- Constraints for table `recipe_ingredients`
--
ALTER TABLE `recipe_ingredients`
  ADD CONSTRAINT `recipe_ingredients_ibfk_1` FOREIGN KEY (`recipe`) REFERENCES `recipe` (`ID`);

--
-- Constraints for table `recipe_steps`
--
ALTER TABLE `recipe_steps`
  ADD CONSTRAINT `recipe_steps_ibfk_1` FOREIGN KEY (`recipe`) REFERENCES `recipe` (`ID`);
COMMIT;