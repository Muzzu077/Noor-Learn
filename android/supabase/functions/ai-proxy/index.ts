import { serve } from "https://deno.land/std@0.168.0/http/server.ts"

const openRouterApiKey = Deno.env.get("OPENROUTER_API_KEY");
// Using stepfun/step-3.5-flash:free as one of the best free models per user request
const bestFreeModel = "stepfun/step-3.5-flash:free";

serve(async (req: Request) => {
    if (req.method === 'OPTIONS') {
        return new Response('ok', { headers: { 'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Headers': 'authorization, x-client-info, apikey, content-type' } })
    }

    try {
        const authHeader = req.headers.get("Authorization");
        if (!authHeader) {
            return new Response(JSON.stringify({ error: "No authorization header" }), { status: 401 });
        }

        const { messages, systemPrompt } = await req.json();

        const openRouterRequest = {
            model: bestFreeModel,
            messages: [
                { role: "system", content: systemPrompt || "You are an Islamic assistant. Always include Quran or Hadith references. If asked for fatwa-level ruling, respond: Consult a qualified scholar." },
                ...messages
            ]
        };

        const response = await fetch("https://openrouter.ai/api/v1/chat/completions", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${openRouterApiKey}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(openRouterRequest)
        });

        const data = await response.json();

        return new Response(JSON.stringify(data), {
            headers: { "Content-Type": "application/json", 'Access-Control-Allow-Origin': '*' },
            status: 200
        });
    } catch (error) {
        const errorMessage = error instanceof Error ? error.message : String(error);
        return new Response(JSON.stringify({ error: errorMessage }), { status: 500, headers: { 'Access-Control-Allow-Origin': '*' } });
    }
})
