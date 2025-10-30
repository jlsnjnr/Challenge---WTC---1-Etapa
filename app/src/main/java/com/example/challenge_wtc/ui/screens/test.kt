package com.example.challenge_wtc.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.challenge_wtc.R // Importe seu R
import kotlinx.coroutines.launch

// 1. Defina o conteúdo de cada slide
data class OnboardingSlide(
    val imageRes: Int, // Recurso da ilustração
    val title: String,
    val description: String
)

@Composable
fun Test(navController: NavController) {
    // 2. Crie a lista de slides com seu conteúdo
    // (Lembre-se de adicionar estas ilustrações em res/drawable)
    val slides = listOf(
        OnboardingSlide(
            imageRes = R.drawable.ic_onboarding_chat, // Ex: ilustração de chat
            title = "Conecte-se instantaneamente",
            description = "Inicie conversas com seus clientes em tempo real, de forma simples e direta."
        ),
        OnboardingSlide(
            imageRes = R.drawable.ic_onboarding_sales, // Ex: ilustração de vendas
            title = "Impulsione suas vendas",
            description = "Envie campanhas promocionais e novidades diretamente para quem mais importa."
        ),
        OnboardingSlide(
            imageRes = R.drawable.ic_onboarding_org, // Ex: ilustração de organização
            title = "Organize tudo",
            description = "Adicione anotações privadas ao histórico e nunca mais perca uma informação importante."
        )
    )

    // 3. Controle de estado do Pager
    val pagerState = rememberPagerState(pageCount = { slides.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1f2937)) // Seu fundo escuro
    ) {
        // Botão "Pular"
        TextButton(
            onClick = {
                // Navega direto para o login
                navController.navigate("login_screen") {
                    popUpTo(0) // Limpa a pilha de navegação
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(text = "Pular", color = Color.White)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 4. O Pager Horizontal
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f) // Ocupa a maior parte da tela
            ) { pageIndex ->
                // O conteúdo de cada slide
                OnboardingSlideContent(slide = slides[pageIndex])
            }

            // 5. Indicador de página (os pontinhos)
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(slides.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color.White else Color.Gray
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(12.dp)
                    )
                }
            }

            // 6. Botão de Ação (Próximo / Começar)
            Button(
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage < slides.size - 1) {
                            // Vai para o próximo slide
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            // Último slide: vai para o login
                            navController.navigate("login_screen") {
                                popUpTo(0) // Limpa a pilha
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White // Botão branco, como na sua tela de login
                )
            ) {
                Text(
                    // Muda o texto do botão no último slide
                    text = if (pagerState.currentPage == slides.size - 1) "Começar Agora" else "Próximo",
                    color = Color.Black, // Texto preto
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun OnboardingSlideContent(slide: OnboardingSlide) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = slide.imageRes),
            contentDescription = slide.title,
            modifier = Modifier
                .size(250.dp) // Tamanho da ilustração
                .padding(bottom = 40.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = slide.title,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium, // Título grande
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = slide.description,
            color = Color.Gray, // Texto de apoio mais suave
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

// Preview para testar
@Preview(showBackground = true)
@Composable
fun TestPreview() {
    // Você pode mockar o NavController no preview assim
    Test(navController = rememberNavController())
}