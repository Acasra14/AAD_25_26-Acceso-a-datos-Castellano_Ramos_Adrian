-- Primero elimina la función si existe
DROP FUNCTION IF EXISTS count_enrollments(integer);

-- Luego crea la nueva función
CREATE OR REPLACE FUNCTION count_enrollments(p_student_id INT)
RETURNS INT AS $$
DECLARE
total INT;
BEGIN
SELECT COUNT(*) INTO total
FROM matricula
WHERE matricula.id_alumno = p_student_id;
RETURN total;
END;
$$ LANGUAGE plpgsql;
@@