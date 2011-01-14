--Q4. Queries

-- 1.
-- SELECT nickname, age-prisonyears AS yearsNotInPrison FROM Robber WHERE (prisonyears > age/3);

-- 2.
-- Retrieve the nickname and total earnings of those gang members who have earned more than $35000 by robbing banks
-- SELECT nickname, SUM(share) AS TotalEarnings FROM (accomplices NATURAL JOIN robber) GROUP BY nickname HAVING SUM(share) > 35000;

-- 3.
CREATE VIEW planned_robbery AS SELECT DISTINCT Plans.bankname, Plans.city FROM Plans LEFT JOIN Robbery ON (Plans.Bankname, Plans.City) = (Robbery.bankname, Robbery.city) WHERE Robbery.bankname IS NULL;

CREATE VIEW have_account AS SELECT planned_robbery.bankname, planned_robbery.city, COUNT(Has_Account.Bankname) AS count_accounts FROM planned_robbery INNER JOIN Has_Account ON (planned_robbery.bankname, planned_robbery.city)= (Has_Account.bankname, has_Account.city) GROUP BY planned_robbery.bankname, planned_robbery.city;

CREATE VIEW result AS SELECT * FROM have_account ORDER BY have_account.count_accounts DESC;

SELECT * FROM result;


/*SELECT DISTINCT Bank.bankname, Bank.city, COUNT(Has_Account.bankname) AS count_accounts FROM Bank INNER JOIN Has_Account ON (Bank.bankname, bank.city) = (Has_Account.bankname, Has_Account.city) WHERE (Bank.bankname, bank.city) NOT IN (SELECT bankname, city FROM Robbery) AND (bank.bankname, bank.city) IN (SELECT bankname, city FROM Plans) GROUP BY bank.bankname, bank.city ORDER BY count_accounts DESC;*/
