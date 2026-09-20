UPDATE dealers
SET number = 'S/N'
WHERE number IS NULL;

ALTER TABLE dealers
    ALTER COLUMN number SET NOT NULL;