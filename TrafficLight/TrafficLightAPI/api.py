from __future__ import unicode_literals

from django.shortcuts import render, redirect
from django.http import JsonResponse

import json

BlockList = [[[-30,30],[-10,10],"B1"],[[10,30],[30,10],"B2"],[[-30,-10],[-10,-30],"B3"],[[10,-10],[30,-30],"B4"]]

def find_block(x,y):
    for i in BlockList:
        if(i[0][0]<= x <= i[1][0] and i[1][1] <= y <= i[0][1]):
            return i[2]
    else:
        return "ERROR"    


def login(request):
    return JsonResponse({'status': 'Success'})


def LightInfo(request):
    x = request.GET['x']
    y = request.GET['y']
    #CrossWalkId = request.GET['CrossWalkId']

    location = find_block(int(x),int(y))

    return JsonResponse([{'X': x}, {'Y': y}, {'Location': location}], safe=False)
