create table instrument(
    symbol                  varchar                          not null,
    id                      bigserial
        constraint instrument_pkey
            primary key
);

create table market(
    id bigserial constraint market_pkey primary key,
    code varchar not null,
    symbol varchar not null,
    name varchar not null,
    country varchar not null,
    website varchar not null
);