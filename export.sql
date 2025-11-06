--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.2

-- Started on 2024-06-29 10:22:57

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4870 (class 1262 OID 17799)
-- Name: CoffeeDB; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "CoffeeDB" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';


ALTER DATABASE "CoffeeDB" OWNER TO postgres;

\connect "CoffeeDB"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 854 (class 1247 OID 17875)
-- Name: size; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.size AS ENUM (
    'small',
    'medium',
    'large',
    'none'
);


ALTER TYPE public.size OWNER TO postgres;

--
-- TOC entry 875 (class 1247 OID 18257)
-- Name: tipe; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.tipe AS ENUM (
    'ADDONS',
    'COFFEE',
    'CREAM'
);


ALTER TYPE public.tipe OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 18093)
-- Name: discount; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discount (
    discount_id integer NOT NULL,
    discount_pct double precision,
    date_start date NOT NULL,
    valid_until_date date NOT NULL,
    payment_id integer,
    required_product character varying,
    required_type character varying,
    status integer DEFAULT 1
);


ALTER TABLE public.discount OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 18092)
-- Name: discount_discount_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.discount_discount_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.discount_discount_id_seq OWNER TO postgres;

--
-- TOC entry 4871 (class 0 OID 0)
-- Dependencies: 219
-- Name: discount_discount_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.discount_discount_id_seq OWNED BY public.discount.discount_id;


--
-- TOC entry 222 (class 1259 OID 18122)
-- Name: membership; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.membership (
    member_id integer NOT NULL,
    member_name character varying NOT NULL,
    member_phone text NOT NULL,
    total_points integer NOT NULL,
    status integer DEFAULT 1,
    CONSTRAINT member_name_long CHECK ((length((member_name)::text) < 30))
);


ALTER TABLE public.membership OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 18121)
-- Name: membership_member_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.membership_member_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.membership_member_id_seq OWNER TO postgres;

--
-- TOC entry 4872 (class 0 OID 0)
-- Dependencies: 221
-- Name: membership_member_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.membership_member_id_seq OWNED BY public.membership.member_id;


--
-- TOC entry 215 (class 1259 OID 17864)
-- Name: menu_list; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.menu_list (
    product_id character varying NOT NULL,
    product_type character varying NOT NULL,
    product_name character varying NOT NULL,
    status integer DEFAULT 1,
    CONSTRAINT product_id_long CHECK ((length((product_id)::text) < 3)),
    CONSTRAINT product_name_long CHECK ((length((product_name)::text) < 25)),
    CONSTRAINT product_type_long CHECK ((length((product_type)::text) < 20))
);


ALTER TABLE public.menu_list OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 17883)
-- Name: menu_price_list; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.menu_price_list (
    price_id character varying(3) NOT NULL,
    size text NOT NULL,
    price double precision NOT NULL,
    product_id character varying NOT NULL,
    status integer DEFAULT 1
);


ALTER TABLE public.menu_price_list OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17896)
-- Name: payment_method; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_method (
    payment_id integer NOT NULL,
    payment_method character varying NOT NULL,
    bank character varying,
    status integer DEFAULT 1
);


ALTER TABLE public.payment_method OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 17895)
-- Name: payment_method_payment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.payment_method_payment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.payment_method_payment_id_seq OWNER TO postgres;

--
-- TOC entry 4873 (class 0 OID 0)
-- Dependencies: 217
-- Name: payment_method_payment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.payment_method_payment_id_seq OWNED BY public.payment_method.payment_id;


--
-- TOC entry 223 (class 1259 OID 18131)
-- Name: transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transaction (
    transaction_number text NOT NULL,
    transaction_date date NOT NULL,
    transaction_time time without time zone NOT NULL,
    customer_name character varying NOT NULL,
    total_item integer NOT NULL,
    total_transaction double precision NOT NULL,
    payment double precision,
    change double precision,
    point_disc integer,
    point_gain integer,
    payment_id integer NOT NULL,
    member_id integer,
    total_disc double precision,
    total_price double precision NOT NULL,
    cashier_name text NOT NULL,
    CONSTRAINT transaction_number_long CHECK ((length(transaction_number) < 11))
);


ALTER TABLE public.transaction OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 18145)
-- Name: transaction_product_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transaction_product_details (
    product_detail_id integer NOT NULL,
    product_quantity integer NOT NULL,
    total_price double precision NOT NULL,
    total_disc double precision,
    special_request text,
    price_id text NOT NULL,
    transaction_number text NOT NULL,
    discount_id integer
);


ALTER TABLE public.transaction_product_details OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 18144)
-- Name: transaction_product_details_product_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transaction_product_details_product_detail_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.transaction_product_details_product_detail_id_seq OWNER TO postgres;

--
-- TOC entry 4874 (class 0 OID 0)
-- Dependencies: 224
-- Name: transaction_product_details_product_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transaction_product_details_product_detail_id_seq OWNED BY public.transaction_product_details.product_detail_id;


--
-- TOC entry 226 (class 1259 OID 18264)
-- Name: username; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.username (
    username character varying NOT NULL,
    password character varying,
    accounttype character varying
);


ALTER TABLE public.username OWNER TO postgres;

--
-- TOC entry 4675 (class 2604 OID 18096)
-- Name: discount discount_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount ALTER COLUMN discount_id SET DEFAULT nextval('public.discount_discount_id_seq'::regclass);


--
-- TOC entry 4677 (class 2604 OID 18125)
-- Name: membership member_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.membership ALTER COLUMN member_id SET DEFAULT nextval('public.membership_member_id_seq'::regclass);


--
-- TOC entry 4673 (class 2604 OID 17899)
-- Name: payment_method payment_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method ALTER COLUMN payment_id SET DEFAULT nextval('public.payment_method_payment_id_seq'::regclass);


--
-- TOC entry 4679 (class 2604 OID 18148)
-- Name: transaction_product_details product_detail_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_product_details ALTER COLUMN product_detail_id SET DEFAULT nextval('public.transaction_product_details_product_detail_id_seq'::regclass);


--
-- TOC entry 4858 (class 0 OID 18093)
-- Dependencies: 220
-- Data for Name: discount; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.discount VALUES (1, 0.15, '2024-02-01', '2024-03-01', 2, NULL, 'AA', 1);
INSERT INTO public.discount VALUES (2, 0.15, '2024-02-01', '2024-03-01', 3, NULL, 'AA', 1);
INSERT INTO public.discount VALUES (3, 0.1, '2024-03-01', '2024-04-01', 1, NULL, NULL, 1);
INSERT INTO public.discount VALUES (4, 0.1, '2024-03-01', '2024-04-01', 2, 'AA', NULL, 1);
INSERT INTO public.discount VALUES (5, 0.1, '2024-03-01', '2024-04-01', 3, 'AA', NULL, 1);
INSERT INTO public.discount VALUES (6, 0.15, '2024-02-01', '2025-03-01', 2, NULL, 'AA', 1);
INSERT INTO public.discount VALUES (7, 0.15, '2024-02-01', '2025-03-01', 3, NULL, 'AA', 1);
INSERT INTO public.discount VALUES (8, 0.1, '2024-03-01', '2025-04-01', 1, NULL, NULL, 1);
INSERT INTO public.discount VALUES (9, 0.1, '2024-03-01', '2025-04-01', 2, 'AA', NULL, 1);
INSERT INTO public.discount VALUES (10, 0.1, '2024-03-01', '2025-04-01', 3, 'AA', NULL, 1);
INSERT INTO public.discount VALUES (17, 0.3, '2024-05-29', '2024-06-13', 1, 'AC', 'AC', 0);
INSERT INTO public.discount VALUES (18, 0.3, '2024-05-29', '2024-06-13', 1, 'AC', 'AC', 0);
INSERT INTO public.discount VALUES (21, 0.3, '2024-06-04', '2024-07-06', NULL, 'AA', 'AA', 0);
INSERT INTO public.discount VALUES (20, 0.3, '2024-06-02', '2024-07-01', 2, 'AA', 'AA', 0);
INSERT INTO public.discount VALUES (22, 0.3, '2024-02-01', '2024-03-01', 2, 'AA', 'AA', 0);
INSERT INTO public.discount VALUES (24, 0.3, '2024-06-04', '2024-06-27', NULL, NULL, NULL, 0);
INSERT INTO public.discount VALUES (23, 0.3, '2024-05-29', '2024-06-20', 1, 'BA', NULL, 0);
INSERT INTO public.discount VALUES (26, 0.3, '2024-04-28', '2024-08-10', 1, 'AB', 'AB', 0);
INSERT INTO public.discount VALUES (25, 0.3, '2024-06-05', '2024-07-06', 1, 'AB', 'AB', 0);


--
-- TOC entry 4860 (class 0 OID 18122)
-- Dependencies: 222
-- Data for Name: membership; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.membership VALUES (3, '2', '2', 0, 0);
INSERT INTO public.membership VALUES (8, 'Joshua', '0', 0, 0);
INSERT INTO public.membership VALUES (4, 'Kevin', '555', 24, 1);
INSERT INTO public.membership VALUES (5, 'Jonathan', '913', 36, 1);
INSERT INTO public.membership VALUES (6, 'Mary', '333', 25, 1);
INSERT INTO public.membership VALUES (7, 'Steven', '315', 23, 1);
INSERT INTO public.membership VALUES (9, 'Tim', '777', 0, 1);
INSERT INTO public.membership VALUES (10, 'Jackary', '145', 0, 0);
INSERT INTO public.membership VALUES (2, 'Jack', '123', 9, 1);


--
-- TOC entry 4853 (class 0 OID 17864)
-- Dependencies: 215
-- Data for Name: menu_list; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.menu_list VALUES ('AA', 'COFFEE', 'Americano', 1);
INSERT INTO public.menu_list VALUES ('AB', 'COFFEE', 'Coffee Latte', 1);
INSERT INTO public.menu_list VALUES ('AC', 'COFFEE', 'Cappuccino', 1);
INSERT INTO public.menu_list VALUES ('AD', 'COFFEE', 'Dark Mocha', 1);
INSERT INTO public.menu_list VALUES ('BA', 'CREAM', 'Vanilla Cream', 1);
INSERT INTO public.menu_list VALUES ('BB', 'CREAM', 'Green Tea Cream', 1);
INSERT INTO public.menu_list VALUES ('ZA', 'ADDONS', 'Caramel Sauce', 1);
INSERT INTO public.menu_list VALUES ('ZB', 'ADDONS', 'Strawberry Sauce', 1);
INSERT INTO public.menu_list VALUES ('ZC', 'ADDONS', 'Chocolate Sprinkle', 1);
INSERT INTO public.menu_list VALUES ('AE', 'COFFEE', 'AAA', 0);
INSERT INTO public.menu_list VALUES ('AZ', 'COFFEE', 'Caramell Machiato', 0);
INSERT INTO public.menu_list VALUES ('AR', 'COFFEE', 'Caramell Machiato', 0);


--
-- TOC entry 4854 (class 0 OID 17883)
-- Dependencies: 216
-- Data for Name: menu_price_list; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.menu_price_list VALUES ('AA1', 'SMALL', 20000, 'AA', 1);
INSERT INTO public.menu_price_list VALUES ('AA2', 'MEDIUM', 30000, 'AA', 1);
INSERT INTO public.menu_price_list VALUES ('AA3', 'LARGE', 50000, 'AA', 1);
INSERT INTO public.menu_price_list VALUES ('AB1', 'SMALL', 27000, 'AB', 1);
INSERT INTO public.menu_price_list VALUES ('AB2', 'MEDIUM', 45000, 'AB', 1);
INSERT INTO public.menu_price_list VALUES ('AB3', 'LARGE', 53000, 'AB', 1);
INSERT INTO public.menu_price_list VALUES ('AC1', 'SMALL', 38000, 'AC', 1);
INSERT INTO public.menu_price_list VALUES ('AC2', 'MEDIUM', 47000, 'AC', 1);
INSERT INTO public.menu_price_list VALUES ('AC3', 'LARGE', 58000, 'AC', 1);
INSERT INTO public.menu_price_list VALUES ('AD1', 'SMALL', 30000, 'AD', 1);
INSERT INTO public.menu_price_list VALUES ('AD2', 'MEDIUM', 40000, 'AD', 1);
INSERT INTO public.menu_price_list VALUES ('AD3', 'LARGE', 60000, 'AD', 1);
INSERT INTO public.menu_price_list VALUES ('BA1', 'SMALL', 50000, 'BA', 1);
INSERT INTO public.menu_price_list VALUES ('BA2', 'MEDIUM', 55000, 'BA', 1);
INSERT INTO public.menu_price_list VALUES ('BA3', 'LARGE', 61000, 'BA', 1);
INSERT INTO public.menu_price_list VALUES ('BB1', 'SMALL', 50000, 'BB', 1);
INSERT INTO public.menu_price_list VALUES ('BB2', 'MEDIUM', 55000, 'BB', 1);
INSERT INTO public.menu_price_list VALUES ('BB3', 'LARGE', 61000, 'BB', 1);
INSERT INTO public.menu_price_list VALUES ('ZA1', 'SMALL', 5000, 'ZA', 1);
INSERT INTO public.menu_price_list VALUES ('ZA2', 'MEDIUM', 7000, 'ZA', 1);
INSERT INTO public.menu_price_list VALUES ('ZA3', 'LARGE', 10000, 'ZA', 1);
INSERT INTO public.menu_price_list VALUES ('ZB1', 'SMALL', 5000, 'ZB', 1);
INSERT INTO public.menu_price_list VALUES ('ZB2', 'MEDIUM', 7, 'ZB', 1);
INSERT INTO public.menu_price_list VALUES ('ZB3', 'LARGE', 10000, 'ZB', 1);
INSERT INTO public.menu_price_list VALUES ('ZC1', 'SMALL', 5000, 'ZC', 1);
INSERT INTO public.menu_price_list VALUES ('ZC2', 'MEDIUM', 7000, 'ZC', 1);
INSERT INTO public.menu_price_list VALUES ('ZC3', 'LARGE', 10000, 'ZC', 1);
INSERT INTO public.menu_price_list VALUES ('AE', 'LARGE', 9000, 'AE', 1);
INSERT INTO public.menu_price_list VALUES ('AZ', 'LARGE', 10000, 'AZ', 0);
INSERT INTO public.menu_price_list VALUES ('AR', 'LARGE', 10000, 'AR', 0);


--
-- TOC entry 4856 (class 0 OID 17896)
-- Dependencies: 218
-- Data for Name: payment_method; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.payment_method VALUES (1, 'Cash', NULL, 1);
INSERT INTO public.payment_method VALUES (2, 'Credit Card', 'PetraBank', 1);
INSERT INTO public.payment_method VALUES (3, 'Debit Card', 'PetraBank', 1);
INSERT INTO public.payment_method VALUES (4, 'CREDIT_CARD', 'asd', 0);
INSERT INTO public.payment_method VALUES (5, 'ALL', '', 1);
INSERT INTO public.payment_method VALUES (6, 'DEBIT_CARD', 'CIMB Niaga', 0);
INSERT INTO public.payment_method VALUES (7, 'DEBIT_CARD', 'test', 0);
INSERT INTO public.payment_method VALUES (8, 'CREDIT_CARD', 'CIMB Niaga', 0);


--
-- TOC entry 4861 (class 0 OID 18131)
-- Dependencies: 223
-- Data for Name: transaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.transaction VALUES ('2024062701', '2024-06-27', '01:03:20', 'a', 1, 4500, 10000, 5500, 0, 0, 1, NULL, 500, 5000, 'b');
INSERT INTO public.transaction VALUES ('2024062702', '2024-06-27', '22:34:01', '1', 2, 88250, 0, 0, 0, 9, 2, 2, 6750, 95000, 'zxc');
INSERT INTO public.transaction VALUES ('2024062703', '2024-06-27', '22:34:32', 'a', 1, 4500, 10000, 5500, 0, 0, 1, NULL, 500, 5000, 'a');
INSERT INTO public.transaction VALUES ('2024062801', '2024-06-28', '21:42:12', '1', 1, 9000, 200000, 191000, 9, 1, 1, 2, 1000, 10000, 'jack');
INSERT INTO public.transaction VALUES ('2024062802', '2024-06-28', '22:03:53', '1', 1, 52190, 100000, 47810, 10, 5, 1, 2, 5800, 58000, 'john');
INSERT INTO public.transaction VALUES ('2024062901', '2024-06-29', '07:28:12', 'Jack', 7, 220500, 0, 0, 5, 31, 2, 2, 94500, 315000, 'John');
INSERT INTO public.transaction VALUES ('2024062902', '2024-06-29', '07:29:21', 'Jack', 7, 427000, 0, 0, 36, 42, 2, 2, 0, 427000, 'John');
INSERT INTO public.transaction VALUES ('2024062903', '2024-06-29', '07:30:12', 'Jack', 4, 139900, 0, 0, 78, 14, 2, 2, 8100, 148000, 'John');
INSERT INTO public.transaction VALUES ('2024062904', '2024-06-29', '07:30:55', 'Kevin', 3, 91500, 0, 0, 0, 10, 2, 4, 13500, 105000, 'John');
INSERT INTO public.transaction VALUES ('2024062905', '2024-06-29', '07:33:32', 'Kevin', 2, 66000, 0, 0, 10, 6, 2, 4, 0, 66000, 'John');
INSERT INTO public.transaction VALUES ('2024062906', '2024-06-29', '07:34:33', 'Jonathan', 3, 80900, 0, 0, 0, 8, 2, 5, 8100, 89000, 'John');
INSERT INTO public.transaction VALUES ('2024062907', '2024-06-29', '07:35:33', 'Mary', 2, 105000, 0, 0, 0, 10, 2, 6, 0, 105000, 'John');
INSERT INTO public.transaction VALUES ('2024062908', '2024-06-29', '07:36:10', 'Steven', 2, 79900, 0, 0, 0, 8, 2, 7, 8100, 88000, 'John');
INSERT INTO public.transaction VALUES ('2024062909', '2024-06-29', '07:36:44', 'Jack', 1, 55000, 0, 0, 92, 5, 2, 2, 0, 55000, 'John');
INSERT INTO public.transaction VALUES ('2024062910', '2024-06-29', '07:37:18', 'Kevin', 3, 60900, 0, 0, 16, 8, 2, 4, 23100, 84000, 'John');
INSERT INTO public.transaction VALUES ('2024062911', '2024-06-29', '07:38:31', 'Jonathan', 3, 133200, 150000, 16800, 8, 16, 1, 5, 34800, 168000, 'John');
INSERT INTO public.transaction VALUES ('2024062912', '2024-06-29', '07:39:37', 'Jonathan', 3, 108900, 200000, 91100, 24, 12, 1, 5, 12100, 121000, 'John');
INSERT INTO public.transaction VALUES ('2024062913', '2024-06-29', '07:40:57', 'Mary', 3, 127200, 250000, 122800, 10, 15, 1, 6, 26800, 154000, 'John');
INSERT INTO public.transaction VALUES ('2024062914', '2024-06-29', '07:41:50', 'Steven', 4, 108900, 200000, 91100, 8, 15, 1, 7, 42100, 151000, 'John');
INSERT INTO public.transaction VALUES ('2024062915', '2024-06-29', '10:16:07', 'Jack', 2, 58103, 100000, 41897, 97, 9, 1, 2, 38800, 97000, 'John');


--
-- TOC entry 4863 (class 0 OID 18145)
-- Dependencies: 225
-- Data for Name: transaction_product_details; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.transaction_product_details VALUES (1, 1, 5000, 0, '-', 'ZA1', '2024062701', NULL);
INSERT INTO public.transaction_product_details VALUES (2, 1, 45000, 6750, '-', 'AB2', '2024062702', 6);
INSERT INTO public.transaction_product_details VALUES (3, 1, 50000, 0, '-', 'BB1', '2024062702', NULL);
INSERT INTO public.transaction_product_details VALUES (4, 1, 5000, 0, '-', 'ZA1', '2024062703', NULL);
INSERT INTO public.transaction_product_details VALUES (5, 1, 10000, 0, 'less sugar', 'ZA3', '2024062801', NULL);
INSERT INTO public.transaction_product_details VALUES (6, 1, 58000, 0, 'less sugar', 'AC3', '2024062802', NULL);
INSERT INTO public.transaction_product_details VALUES (7, 7, 315000, 94500, '-', 'AB2', '2024062901', 20);
INSERT INTO public.transaction_product_details VALUES (8, 7, 427000, 0, '-', 'BA3', '2024062902', NULL);
INSERT INTO public.transaction_product_details VALUES (9, 1, 27000, 8100, '-', 'AB1', '2024062903', 20);
INSERT INTO public.transaction_product_details VALUES (10, 1, 55000, 0, '-', 'BA2', '2024062903', NULL);
INSERT INTO public.transaction_product_details VALUES (11, 1, 5000, 0, '-', 'ZB1', '2024062903', NULL);
INSERT INTO public.transaction_product_details VALUES (12, 1, 61000, 0, '-', 'BB3', '2024062903', NULL);
INSERT INTO public.transaction_product_details VALUES (13, 1, 55000, 0, '-', 'BA2', '2024062904', NULL);
INSERT INTO public.transaction_product_details VALUES (14, 1, 5000, 0, '-', 'ZC1', '2024062904', NULL);
INSERT INTO public.transaction_product_details VALUES (15, 1, 45000, 13500, '-', 'AB2', '2024062904', 20);
INSERT INTO public.transaction_product_details VALUES (16, 1, 61000, 0, '-', 'BA3', '2024062905', NULL);
INSERT INTO public.transaction_product_details VALUES (17, 1, 5000, 0, '-', 'ZB1', '2024062905', NULL);
INSERT INTO public.transaction_product_details VALUES (18, 1, 55000, 0, '-', 'BB2', '2024062906', NULL);
INSERT INTO public.transaction_product_details VALUES (19, 1, 27000, 8100, '-', 'AB1', '2024062906', 20);
INSERT INTO public.transaction_product_details VALUES (20, 1, 7000, 0, '-', 'ZC2', '2024062906', NULL);
INSERT INTO public.transaction_product_details VALUES (21, 1, 50000, 0, '-', 'BB1', '2024062907', NULL);
INSERT INTO public.transaction_product_details VALUES (22, 1, 55000, 0, '-', 'BB2', '2024062907', NULL);
INSERT INTO public.transaction_product_details VALUES (23, 1, 61000, 0, '-', 'BB3', '2024062908', NULL);
INSERT INTO public.transaction_product_details VALUES (24, 1, 27000, 8100, '-', 'AB1', '2024062908', 20);
INSERT INTO public.transaction_product_details VALUES (25, 1, 55000, 0, '-', 'BA2', '2024062909', NULL);
INSERT INTO public.transaction_product_details VALUES (26, 1, 27000, 8100, '-', 'AB1', '2024062910', 20);
INSERT INTO public.transaction_product_details VALUES (27, 1, 7000, 0, '-', 'ZC2', '2024062910', NULL);
INSERT INTO public.transaction_product_details VALUES (28, 1, 50000, 15000, '-', 'AA3', '2024062910', 20);
INSERT INTO public.transaction_product_details VALUES (29, 1, 55000, 0, '-', 'BB2', '2024062911', NULL);
INSERT INTO public.transaction_product_details VALUES (30, 1, 60000, 18000, '-', 'AD3', '2024062911', 21);
INSERT INTO public.transaction_product_details VALUES (31, 1, 53000, 0, '-', 'AB3', '2024062911', NULL);
INSERT INTO public.transaction_product_details VALUES (32, 1, 61000, 0, '-', 'BB3', '2024062912', NULL);
INSERT INTO public.transaction_product_details VALUES (33, 1, 55000, 0, '-', 'BB2', '2024062912', NULL);
INSERT INTO public.transaction_product_details VALUES (34, 1, 5000, 0, '-', 'ZC1', '2024062912', NULL);
INSERT INTO public.transaction_product_details VALUES (35, 1, 55000, 0, '-', 'BB2', '2024062913', NULL);
INSERT INTO public.transaction_product_details VALUES (36, 1, 38000, 11400, '-', 'AC1', '2024062913', 21);
INSERT INTO public.transaction_product_details VALUES (37, 1, 61000, 0, '-', 'BB3', '2024062913', NULL);
INSERT INTO public.transaction_product_details VALUES (38, 1, 61000, 0, '-', 'BB3', '2024062914', NULL);
INSERT INTO public.transaction_product_details VALUES (39, 3, 90000, 27000, '-', 'AD1', '2024062914', 21);
INSERT INTO public.transaction_product_details VALUES (40, 1, 47000, 14100, 'less sugar', 'AC2', '2024062915', 25);
INSERT INTO public.transaction_product_details VALUES (41, 1, 50000, 15000, '-', 'AA3', '2024062915', 25);


--
-- TOC entry 4864 (class 0 OID 18264)
-- Dependencies: 226
-- Data for Name: username; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.username VALUES ('master', 'master', 'MASTER');
INSERT INTO public.username VALUES ('cashier', 'cashier', 'CASHIER');


--
-- TOC entry 4875 (class 0 OID 0)
-- Dependencies: 219
-- Name: discount_discount_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discount_discount_id_seq', 26, true);


--
-- TOC entry 4876 (class 0 OID 0)
-- Dependencies: 221
-- Name: membership_member_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.membership_member_id_seq', 10, true);


--
-- TOC entry 4877 (class 0 OID 0)
-- Dependencies: 217
-- Name: payment_method_payment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.payment_method_payment_id_seq', 8, true);


--
-- TOC entry 4878 (class 0 OID 0)
-- Dependencies: 224
-- Name: transaction_product_details_product_detail_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transaction_product_details_product_detail_id_seq', 41, true);


--
-- TOC entry 4692 (class 2606 OID 18100)
-- Name: discount discount_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_pkey PRIMARY KEY (discount_id);


--
-- TOC entry 4694 (class 2606 OID 18130)
-- Name: membership membership_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.membership
    ADD CONSTRAINT membership_pkey PRIMARY KEY (member_id);


--
-- TOC entry 4686 (class 2606 OID 17873)
-- Name: menu_list menu_list_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.menu_list
    ADD CONSTRAINT menu_list_pkey PRIMARY KEY (product_id);


--
-- TOC entry 4688 (class 2606 OID 17889)
-- Name: menu_price_list menu_price_list_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.menu_price_list
    ADD CONSTRAINT menu_price_list_pkey PRIMARY KEY (price_id);


--
-- TOC entry 4690 (class 2606 OID 17903)
-- Name: payment_method payment_method_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method
    ADD CONSTRAINT payment_method_pkey PRIMARY KEY (payment_id);


--
-- TOC entry 4696 (class 2606 OID 18190)
-- Name: transaction transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (transaction_number);


--
-- TOC entry 4698 (class 2606 OID 18152)
-- Name: transaction_product_details transaction_product_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_product_details
    ADD CONSTRAINT transaction_product_details_pkey PRIMARY KEY (product_detail_id);


--
-- TOC entry 4700 (class 2606 OID 18270)
-- Name: username username_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.username
    ADD CONSTRAINT username_pkey PRIMARY KEY (username);


--
-- TOC entry 4702 (class 2606 OID 18101)
-- Name: discount discount_payment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_payment_id_fkey FOREIGN KEY (payment_id) REFERENCES public.payment_method(payment_id) ON DELETE CASCADE;


--
-- TOC entry 4703 (class 2606 OID 18106)
-- Name: discount discount_required_product_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_required_product_fkey FOREIGN KEY (required_product) REFERENCES public.menu_list(product_id) ON DELETE CASCADE;


--
-- TOC entry 4704 (class 2606 OID 18111)
-- Name: discount discount_required_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_required_type_fkey FOREIGN KEY (required_type) REFERENCES public.menu_list(product_id) ON DELETE CASCADE;


--
-- TOC entry 4705 (class 2606 OID 18215)
-- Name: transaction fk_member; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES public.membership(member_id) ON DELETE CASCADE;


--
-- TOC entry 4701 (class 2606 OID 17890)
-- Name: menu_price_list menu_price_list_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.menu_price_list
    ADD CONSTRAINT menu_price_list_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.menu_list(product_id) ON DELETE CASCADE;


--
-- TOC entry 4706 (class 2606 OID 18139)
-- Name: transaction transaction_payment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_payment_id_fkey FOREIGN KEY (payment_id) REFERENCES public.payment_method(payment_id) ON DELETE CASCADE;


--
-- TOC entry 4707 (class 2606 OID 18163)
-- Name: transaction_product_details transaction_product_details_discount_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_product_details
    ADD CONSTRAINT transaction_product_details_discount_id_fkey FOREIGN KEY (discount_id) REFERENCES public.discount(discount_id) ON DELETE CASCADE;


--
-- TOC entry 4708 (class 2606 OID 18179)
-- Name: transaction_product_details transaction_product_details_price_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_product_details
    ADD CONSTRAINT transaction_product_details_price_id_fkey FOREIGN KEY (price_id) REFERENCES public.menu_price_list(price_id) ON DELETE CASCADE;


--
-- TOC entry 4709 (class 2606 OID 18192)
-- Name: transaction_product_details transaction_product_details_transaction_number_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_product_details
    ADD CONSTRAINT transaction_product_details_transaction_number_fkey FOREIGN KEY (transaction_number) REFERENCES public.transaction(transaction_number) ON DELETE CASCADE;


-- Completed on 2024-06-29 10:22:57

--
-- PostgreSQL database dump complete
--

