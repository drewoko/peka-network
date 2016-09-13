“екущий API:

/getRecommendations/{name} - возращает рекомендации дл€ пользовател€, основанные на его текущих предпочтени€х
ответ:
{
  [channelSlug: string, // »дентификатор канала
   ...
  ]
}

/getUsersFavouriteStreams/{name} - возращает 10 любимых стримов пользовател€, упор€доченных по возрастанию предпочтени€
ответ:
{
  [channelSlug: string, // »дентификатор канала
   ...
  ]
}

/getStreamRating - возращает 15 стримов, имеющих наибольший рейтинг, основанный на размере аудитории канала. —тримы упор€дочены по возростанию рейтинга.
ответ:
{
  [channelDescriptor:{
       channelSlug: string, // »дентификатор канала
       estimate: int // –ейтинг канала
   },
   ...
  ]
}

/lastDayUserStatistic/{name} - возращает историю просмотров пользовател€ за последние 24 часа
ответ:
{
  [
    {
       startDate: long,
       endDate: long,
       streamSlug: string,
       userName: string
    },
    ...
  ]
}

/lastDayStreamActivities/{name} - возращает историю активностей пользователей на канале за последние 24 часа
ответ:
{
  [
    {
       startDate: long,
       endDate: long,
       messageCount: int
       userCount: int
    },
    ...
  ]
}

/findUser/{name} - возращает каналы, на которых на данный момент находитс€ пользователь
ответ:
{
  string //—сылки на каналы через пробел
}

/getStreamStatus/{name} - возращает статус канала(онлайн либо оффлайн)
ответ:
{
  string
}