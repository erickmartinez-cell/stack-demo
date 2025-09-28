

-- Informe de resumen de comentarios únicos de GlobalStay (PostgreSQL)
WITH categorized AS (
    SELECT DISTINCT reviewer_id,
        CASE
            WHEN rating IS NULL THEN 'Without Rating'
            WHEN rating > 0 THEN 'Positive'
            WHEN rating < 0 THEN 'Negative'
        END AS feedback_type
    FROM feedbacks
)
SELECT feedback_type, total_unique_feedbacks
FROM (
    SELECT feedback_type, COUNT(*) AS total_unique_feedbacks
    FROM categorized
    GROUP BY feedback_type
    UNION ALL
    SELECT 'All Types', COUNT(*) AS total_unique_feedbacks
    FROM categorized
) sub
ORDER BY CASE feedback_type
    WHEN 'Positive' THEN 1
    WHEN 'Negative' THEN 2
    WHEN 'Without Rating' THEN 3
    WHEN 'All Types' THEN 4
    ELSE 5
END;
