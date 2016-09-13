������� API:

/getRecommendations/{name} - ��������� ������������ ��� ������������, ���������� �� ��� ������� �������������
�����:
{
  [channelSlug: string, // ������������� ������
   ...
  ]
}

/getUsersFavouriteStreams/{name} - ��������� 10 ������� ������� ������������, ������������� �� ����������� ������������
�����:
{
  [channelSlug: string, // ������������� ������
   ...
  ]
}

/getStreamRating - ��������� 15 �������, ������� ���������� �������, ���������� �� ������� ��������� ������. ������ ����������� �� ����������� ��������.
�����:
{
  [channelDescriptor:{
       channelSlug: string, // ������������� ������
       estimate: int // ������� ������
   },
   ...
  ]
}

/lastDayUserStatistic/{name} - ��������� ������� ���������� ������������ �� ��������� 24 ����
�����:
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

/lastDayStreamActivities/{name} - ��������� ������� ����������� ������������� �� ������ �� ��������� 24 ����
�����:
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

/findUser/{name} - ��������� ������, �� ������� �� ������ ������ ��������� ������������
�����:
{
  string //������ �� ������ ����� ������
}

/getStreamStatus/{name} - ��������� ������ ������(������ ���� �������)
�����:
{
  string
}