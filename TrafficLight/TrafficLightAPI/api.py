from __future__ import unicode_literals

from django.shortcuts import render, redirect
from django.http import JsonResponse

import json

H1 = [[0,0],[4,4]]

def login(request):
    return JsonResponse({'status': 'Success'})


def LightInfo(request):
    x = request.GET['x']
    y = request.GET['y']
    #CrossWalkId = request.GET['CrossWalkId']

    if H1[1][1] >= int(x) >= H1[0][0]:
        location = "H1"

    return JsonResponse([{'X': x}, {'Y': y}, {'Location': location}], safe=False)
