// Limit the mapview to inside a polygon drawn by user using Leaflet
// Receives a Leaflet draw event and the map object
function adaptViewToPol(e, map) {

    var type = e.layerType,
    layer = e.layer;

    switch(type) {
        case 'polygon':
        case 'rectangle':

            var coords = layer.getLatLngs();

            var southWest = L.latLng(coords[1].lat, coords[1].lng);
            var northEast = L.latLng(coords[3].lat, coords[3].lng);

        case 'circle':
            // TODO: Get center and radius and build buffer

            break;

        default:
            // Do nothing
            console.log(type);
            break;
    }

    // Center the map in the polygon drawn
    var bounds = L.latLngBounds(southWest, northEast);
    map.fitBounds(bounds);
}

/* Main function */
function main() {
    var map = L.map('map').setView([40.416775, -3.703790], 12);
        mapLink =
            '<a href="http://openstreetmap.org">OpenStreetMap</a>';


        L.tileLayer(
            'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '&copy; ' + mapLink + ' Contributors',
            maxZoom: 18,
            }).addTo(map);


        defaultZoom = map.getZoom();
        defaultBounds = map.getBounds();

        // Add Leaflet draw stuff
        var drawnItems = new L.FeatureGroup();
        map.addLayer(drawnItems);

        var drawControl = new L.Control.Draw({
            draw: {
                position: 'bottomleft',
                polygon: {
                    shapeOptions: {
                        color: 'purple'
                    },
                    allowIntersection: false,
                    drawError: {
                        color: 'orange',
                        timeout: 1000
                    },
                    showArea: true,
                    metric: false,
                    repeatMode: true
                },

                rect: {
                    shapeOptions: {
                        color: 'green'
                    },
                },

                polyline: false,// Turns off this drawing tool
                circle: false,
                marker: false

            },
            edit: {
                featureGroup: drawnItems
            }

            });

        map.addControl(drawControl);

        // Each time we draw a polygon or rectangle, adjust the view
        map.on('draw:created', function (e) {
            adaptViewToPol(e, map);

        });

        // Each time we draw a polygon or rectangle, get the data just inside that pol/rect
        map.on('draw:edited', function (e) {
            adaptViewToPol(e, map);
        });


        map.on('draw:deleted', function (e) {
            console.log("Reset");

            // Reset
            map.fitBounds(defaultBounds);
            map.setZoom(defaultZoom);
        });
}
